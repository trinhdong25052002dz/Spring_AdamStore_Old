package Spring_AdamStore.service.impl;

import Spring_AdamStore.constants.OrderStatus;
import Spring_AdamStore.constants.PaymentMethod;
import Spring_AdamStore.constants.PaymentStatus;
import Spring_AdamStore.constants.RoleEnum;
import Spring_AdamStore.dto.request.*;
import Spring_AdamStore.dto.response.*;
import Spring_AdamStore.entity.*;
import Spring_AdamStore.entity.Order;
import Spring_AdamStore.entity.relationship.PromotionUsage;
import Spring_AdamStore.exception.AppException;
import Spring_AdamStore.exception.ErrorCode;
import Spring_AdamStore.mapper.OrderMapper;
import Spring_AdamStore.repository.*;
import Spring_AdamStore.repository.criteria.SearchCriteriaQueryConsumer;
import Spring_AdamStore.repository.criteria.SearchCriteria;
import Spring_AdamStore.repository.relationship.PromotionUsageRepository;
import Spring_AdamStore.service.CurrentUserService;
import Spring_AdamStore.service.OrderService;
import Spring_AdamStore.service.PageableService;
import Spring_AdamStore.service.PaymentHistoryService;
import Spring_AdamStore.service.relationship.PromotionUsageService;
import Spring_AdamStore.service.relationship.UserHasRoleService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Spring_AdamStore.constants.OrderStatus.PENDING;

@Service
@Slf4j(topic = "ORDER-SERVICE")
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    private final CurrentUserService currentUserService;
    private final AddressRepository addressRepository;
    private final ProductVariantRepository productVariantRepository;
    private final OrderItemRepository orderItemRepository;
    private final PromotionRepository promotionRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final UserHasRoleService userHasRoleService;
    private final PageableService pageableService;
    private final PromotionUsageService promotionUsageService;
    private final PromotionUsageRepository promotionUsageRepository;
    private final PaymentHistoryService paymentHistoryService;
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    @Transactional
    public OrderResponse create(OrderRequest request) {
        // user
        User user = currentUserService.getCurrentUser();

        // address
        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_EXISTED));

        // ShippingFee
        double totalPrice = request.getShippingFee();

        OrderStatus orderStatus = request.getPaymentMethod() == PaymentMethod.CASH
                ? OrderStatus.PROCESSING
                : OrderStatus.PENDING;

        Order order = Order.builder()
                .user(user)
                .address(address)
                .orderStatus(orderStatus)
                .totalPrice(totalPrice)
                .orderDate(LocalDate.now())
                .build();

        orderRepository.save(order);

        // orderItem
        Set<OrderItem> orderItemSet = new HashSet<>();
        for(OrderItemRequest orderItemRequest : request.getOrderItems()){
            ProductVariant productVariant = productVariantRepository.findById(orderItemRequest.getProductVariantId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_EXISTED));

            // check sl hang con
            if (productVariant.getQuantity() < orderItemRequest.getQuantity()) {
                throw new AppException(ErrorCode.OUT_OF_STOCK);
            }

            OrderItem orderItem = OrderItem.builder()
                    .quantity(orderItemRequest.getQuantity())
                    .unitPrice(productVariant.getPrice())
                    .order(order)
                    .productVariant(productVariant)
                    .build();

            // tru so luong hang trong kho
            productVariant.setQuantity(productVariant.getQuantity() - orderItemRequest.getQuantity());
            productVariantRepository.save(productVariant);

            orderItemSet.add(orderItem);
            totalPrice += orderItem.getQuantity() * orderItem.getUnitPrice();
        }
        orderItemRepository.saveAll(orderItemSet);
        order.setOrderItems(orderItemSet);

        // Promotion
        if (request.getPromotionId() != null) {
            totalPrice = applyPromotion(request.getPromotionId(), order, user, totalPrice);
        }

        order.setTotalPrice(totalPrice);

        // PaymentHistory
        paymentHistoryService.savePaymentHistory(order, request.getPaymentMethod());

        return orderMapper.toOrderResponse(orderRepository.save(order));
    }


    @Override
    public OrderResponse fetchById(Long id) {
        Order order = findOrderById(id);

        return orderMapper.toOrderResponse(order);
    }

    @Override
    public PageResponse<OrderResponse> fetchAll(int pageNo, int pageSize, String sortBy) {
        pageNo = pageNo - 1;

        Pageable pageable = pageableService.createPageable(pageNo, pageSize, sortBy, Order.class);

        Page<Order> orderPage = orderRepository.findAll(pageable);

        return PageResponse.<OrderResponse>builder()
                .page(orderPage.getNumber() + 1)
                .size(orderPage.getSize())
                .totalPages(orderPage.getTotalPages())
                .totalItems(orderPage.getTotalElements())
                .items(orderMapper.toOrderResponseList(orderPage.getContent()))
                .build();
    }

    @Override
    public PageResponse<OrderResponse> searchOrder(int pageNo, int pageSize, String sortBy, List<String> search) {
        pageNo = pageNo - 1;

        List<SearchCriteria> criteriaList = new ArrayList<>();

        // Lay danh sach dieu kien
        if(search != null){
            for(String s : search){
                Pattern pattern = Pattern.compile("(\\w+?)(~|>|<)(.*)");
                Matcher matcher = pattern.matcher(s);
                if(matcher.find()){
                    criteriaList.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
                }
            }
        }
        List<Order> orderList = getOrderList(pageNo, pageSize, sortBy, criteriaList);

        Long totalElements = getTotalElements(criteriaList);
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        return PageResponse.<OrderResponse>builder()
                .page(pageNo + 1)
                .size(pageSize)
                .totalPages(totalPages)
                .totalItems(totalElements)
                .items(orderMapper.toOrderResponseList(orderList))
                .build();
    }

    private List<Order> getOrderList(int pageNo, int pageSize, String sortBy, List<SearchCriteria> criteriaList) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = builder.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);

        // xu ly dieu kien
        Predicate predicate = builder.conjunction();

        // check user
        User currentUser = currentUserService.getCurrentUser();
        boolean isAdmin = userHasRoleService.checkRoleForUser(currentUser, RoleEnum.ADMIN);
        if (!isAdmin) {
            Join<Order, User> userJoin = root.join("user", JoinType.LEFT);
            predicate = builder.and(predicate, builder.equal(userJoin.get("id"), currentUser.getId()));
        }

        // search
        if(!CollectionUtils.isEmpty(criteriaList)){
            SearchCriteriaQueryConsumer queryConsumer = new SearchCriteriaQueryConsumer(builder, predicate, root);
            criteriaList.forEach(queryConsumer);

            predicate = builder.and(predicate, queryConsumer.getPredicate());
        }

        query.where(predicate);

        // Sort
        if(StringUtils.hasLength(sortBy)){
            Pattern pattern = Pattern.compile("(\\w+?)(-)(asc|desc)");
            Matcher matcher = pattern.matcher(sortBy);
            if(matcher.find()){
                String columnName = matcher.group(1);

                if(matcher.group(3).equalsIgnoreCase("desc")){
                    query.orderBy(builder.desc(root.get(columnName)));
                }else{
                    query.orderBy(builder.asc(root.get(columnName)));
                }
            }
        }

        return entityManager.createQuery(query)
                .setFirstResult(pageNo * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    private Long getTotalElements(List<SearchCriteria> criteriaList) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<Order> root = countQuery.from(Order.class);

        // Xu ly dieu kien tim kiem
        Predicate predicate = builder.conjunction();

        // check user
        User currentUser = currentUserService.getCurrentUser();
        boolean isAdmin = userHasRoleService.checkRoleForUser(currentUser, RoleEnum.ADMIN);
        if (!isAdmin) {
            Join<Order, User> userJoin = root.join("user", JoinType.LEFT);
            predicate = builder.and(predicate, builder.equal(userJoin.get("id"), currentUser.getId()));
        }

        // search
        if(!CollectionUtils.isEmpty(criteriaList)){
            SearchCriteriaQueryConsumer queryConsumer = new SearchCriteriaQueryConsumer(builder, predicate, root);
            criteriaList.forEach(queryConsumer);

            predicate = builder.and(predicate, queryConsumer.getPredicate());
        }

        countQuery.select(builder.count(root));
        countQuery.where(predicate);

        return entityManager.createQuery(countQuery).getSingleResult();
    }

    @Override
    @Transactional
    public OrderResponse updateAddress(Long id, UpdateOrderAddressRequest request) {
        Order order = findOrderById(id);

        String currentUserEmail = currentUserService.getCurrentUsername();

        if (!order.getUser().getEmail().equals(currentUserEmail)) {
            throw new AppException(ErrorCode.ORDER_NOT_BELONG_TO_USER);
        }

        if (!(order.getOrderStatus() == OrderStatus.PENDING || order.getOrderStatus() == OrderStatus.PROCESSING)) {
            throw new AppException(ErrorCode.ORDER_CANNOT_UPDATE_ADDRESS);
        }

        Address newAddress = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_EXISTED));

        if (!newAddress.getUser().getEmail().equals(currentUserEmail)) {
            throw new AppException(ErrorCode.ADDRESS_NOT_BELONG_TO_USER);
        }

        order.setAddress(newAddress);
        return orderMapper.toOrderResponse(orderRepository.save(order));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Order order = findOrderById(id);

        orderItemRepository.deleteAll(order.getOrderItems());

        paymentHistoryRepository.deleteAll(order.getPayments());

        promotionUsageRepository.delete(order.getPromotionUsage());

        orderRepository.delete(order);
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));
    }

    private double applyPromotion(Long promotionId, Order order, User user, double currentTotal) {
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new AppException(ErrorCode.PROMOTION_NOT_EXISTED));

        PromotionUsage usage = promotionUsageService.applyPromotion(promotion, order, user, currentTotal);

        order.setPromotionUsage(usage);
        log.info("Usage Discount Amount: {}", usage.getDiscountAmount());
        log.info("Discount : {}", currentTotal * usage.getDiscountAmount());
        return currentTotal - usage.getDiscountAmount();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void updateOrderStatusProcessingToShipped() {
        log.info("Update Order From Processing To Shipped");

        LocalDate currentDate = LocalDate.now();

        List<Order> orderList = orderRepository.findByOrderStatusAndOrderDateBefore(OrderStatus.PROCESSING,
                currentDate.minusDays(1));

        orderList.forEach(order ->  order.setOrderStatus(OrderStatus.SHIPPED));
        orderRepository.saveAll(orderList);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void updateOrderStatusShippedToDelivered() {
        log.info("Update Order From Shipped To Delivered");

        LocalDate currentDate = LocalDate.now();

        List<Order> orderList = orderRepository.findByOrderStatusAndOrderDateBefore(OrderStatus.SHIPPED,
                currentDate.minusDays(3));

        orderList.forEach(order -> {
            order.setOrderStatus(OrderStatus.DELIVERED);

            updatePendingCashPaymentsToPaid(order);
        });

        orderRepository.saveAll(orderList);
    }

    private void updatePendingCashPaymentsToPaid(Order order) {
        List<PaymentHistory> updatedPayments = new ArrayList<>();

        order.getPayments().stream()
                .filter(payment -> payment.getPaymentMethod() == PaymentMethod.CASH
                        && payment.getPaymentStatus() == PaymentStatus.PENDING)
                .forEach(payment -> {
                    payment.setIsPrimary(true);
                    payment.setPaymentStatus(PaymentStatus.PAID);
                    updatedPayments.add(payment);
                });

        paymentHistoryRepository.saveAll(updatedPayments);
    }


    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void cancelPendingOrdersOverOneDay() {
        log.info("Cancel Pending Orders Over One Day");

        LocalDate currentDate = LocalDate.now();

        List<Order> orderList = orderRepository.findByOrderStatusAndOrderDateBefore(PENDING,
                currentDate.minusDays(1));

        orderList.forEach(order ->  order.setOrderStatus(OrderStatus.CANCELLED));
        orderRepository.saveAll(orderList);
    }

}
