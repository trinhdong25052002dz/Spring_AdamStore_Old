package Spring_AdamStore.service.impl;

import Spring_AdamStore.constants.EntityStatus;
import Spring_AdamStore.constants.OrderStatus;
import Spring_AdamStore.constants.RoleEnum;
import Spring_AdamStore.dto.request.UserCreationRequest;
import Spring_AdamStore.dto.request.UserUpdateRequest;
import Spring_AdamStore.dto.response.AddressResponse;
import Spring_AdamStore.dto.response.PageResponse;
import Spring_AdamStore.dto.response.PromotionResponse;
import Spring_AdamStore.dto.response.UserResponse;
import Spring_AdamStore.entity.*;
import Spring_AdamStore.entity.relationship.UserHasRole;
import Spring_AdamStore.exception.AppException;
import Spring_AdamStore.exception.ErrorCode;
import Spring_AdamStore.mapper.AddressMapper;
import Spring_AdamStore.mapper.PromotionMapper;
import Spring_AdamStore.mapper.UserMapper;
import Spring_AdamStore.repository.*;
import Spring_AdamStore.repository.relationship.PromotionUsageRepository;
import Spring_AdamStore.repository.relationship.UserHasRoleRepository;
import Spring_AdamStore.service.CartService;
import Spring_AdamStore.service.CurrentUserService;
import Spring_AdamStore.service.PageableService;
import Spring_AdamStore.service.UserService;
import Spring_AdamStore.service.relationship.UserHasRoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static Spring_AdamStore.constants.EntityStatus.ACTIVE;

@Service
@Slf4j(topic = "USER-SERVICE")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PageableService pageableService;
    private final UserHasRoleService userHasRoleService;
    private final RoleRepository roleRepository;
    private final UserHasRoleRepository userHasRoleRepository;
    private final AddressRepository addressRepository;
    private final CartService cartService;
    private final AddressMapper addressMapper;
    private final PromotionRepository promotionRepository;
    private final OrderRepository orderRepository;
    private final CurrentUserService currentUserService;
    private final PromotionUsageRepository promotionUsageRepository;
    private final PromotionMapper promotionMapper;


    @Override
    @Transactional
    public UserResponse create(UserCreationRequest request) {
        checkEmailExist(request.getEmail());
        User user = userMapper.userCreationToUser(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        // role user mac dinh
        Set<UserHasRole> roles = new HashSet<>();
        roles.add(userHasRoleService.saveUserHasRole(user, RoleEnum.USER));
        if(!CollectionUtils.isEmpty(request.getRoleIds())){
            Set<Role> roleSet = roleRepository.findAllByIdIn(request.getRoleIds());

            Set<UserHasRole> userRoles = roleSet.stream()
                    .filter(role -> !role.getName().equals(RoleEnum.USER.toString()))
                    .map(role -> new UserHasRole(user, role))
                    .collect(Collectors.toSet());

            roles.addAll(userHasRoleRepository.saveAll(userRoles));
        }
        user.setRoles(roles);

        cartService.createCartForUser(user);

        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse fetchUserById(Long id) {
        User userDB = findActiveUserById(id);

        return userMapper.toUserResponse(userDB);
    }

    @Override
    public PageResponse<UserResponse> fetchAllUsers(int pageNo, int pageSize, String sortBy) {
        pageNo = pageNo - 1;

        Pageable pageable = pageableService.createPageable(pageNo, pageSize, sortBy, User.class);

        Page<User> userPage = userRepository.findAllUsers(pageable);

        return PageResponse.<UserResponse>builder()
                .page(userPage.getNumber() + 1)
                .size(userPage.getSize())
                .totalPages(userPage.getTotalPages())
                .totalItems(userPage.getTotalElements())
                .items(userMapper.toUserResponseList(userPage.getContent()))
                .build();
    }

    @Override
    @Transactional
    public UserResponse update(Long id, UserUpdateRequest request) {
        User userDB = findActiveUserById(id);

        userMapper.updateUser(userDB, request);

        if(!CollectionUtils.isEmpty(request.getRoleIds())){
            userHasRoleRepository.deleteByUser(userDB);

            Set<UserHasRole> roles = new HashSet<>();
            // role user mac dinh
            roles.add(userHasRoleService.saveUserHasRole(userDB, RoleEnum.USER));

            Set<Role> roleSet = roleRepository.findAllByIdIn(request.getRoleIds());

            Set<UserHasRole> userRoles = roleSet.stream()
                    .filter(role -> !role.getName().equals(RoleEnum.USER.toString()))
                    .map(role -> new UserHasRole(userDB, role))
                    .collect(Collectors.toSet());

            roles.addAll(userHasRoleRepository.saveAll(userRoles));

            userDB.setRoles(roles);
        }

        return userMapper.toUserResponse(userRepository.save(userDB));
    }

    @Override
    public void delete(Long id) {
        User userDB = findActiveUserById(id);

        List<OrderStatus> activeStatuses = List.of(OrderStatus.PROCESSING, OrderStatus.SHIPPED, OrderStatus.DELIVERED);
        if(orderRepository.existsByUserIdAndOrderStatusIn(userDB.getId(), activeStatuses)){
            throw new AppException(ErrorCode.USER_HAS_ACTIVE_ORDER);
        }

        if(promotionUsageRepository.existsByUserId(userDB.getId())){
            throw new AppException(ErrorCode.USER_HAS_ACTIVE_PROMOTION_USAGE);
        }

        userRepository.delete(userDB);
    }

    @Override
    public UserResponse restore(long id) {
        User userDB = userRepository.findUserById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userDB.setStatus(ACTIVE);
        return userMapper.toUserResponse(userRepository.save(userDB));
    }

    @Override
    public PageResponse<AddressResponse> getAllAddressesByUser(int pageNo, int pageSize, String sortBy) {
        pageNo = pageNo - 1;

        Pageable pageable = pageableService.createPageable(pageNo, pageSize, sortBy, Address.class);

        User user = currentUserService.getCurrentUser();

        Page<Address> addressPage = addressRepository.findAllByUserIdAndIsVisible(user.getId(), true, pageable);

        return PageResponse.<AddressResponse>builder()
                .page(addressPage.getNumber() + 1)
                .size(addressPage.getSize())
                .totalPages(addressPage.getTotalPages())
                .totalItems(addressPage.getTotalElements())
                .items(addressMapper.toAddressResponseList(addressPage.getContent()))
                .build();
    }

    @Override
    public PageResponse<PromotionResponse> getPromotionsByUser(int pageNo, int pageSize, String sortBy) {
        pageNo = pageNo - 1;

        Pageable pageable = pageableService.createPageable(pageNo, pageSize, sortBy, Promotion.class);

        User user = currentUserService.getCurrentUser();

        Page<Promotion> promotionPage = promotionRepository.findAllAvailableForCustomer(user.getId(), LocalDate.now(), pageable);

        return PageResponse.<PromotionResponse>builder()
                .page(promotionPage.getNumber() + 1)
                .size(promotionPage.getSize())
                .totalPages(promotionPage.getTotalPages())
                .totalItems(promotionPage.getTotalElements())
                .items(promotionMapper.toPromotionResponseList(promotionPage.getContent()))
                .build();
    }

    private void checkEmailExist(String email) {
        if (userRepository.countByEmailAndStatus(email, ACTIVE.name()) > 0) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        if (userRepository.countByEmailAndStatus(email, EntityStatus.INACTIVE.name()) > 0) {
            throw new AppException(ErrorCode.EMAIL_DISABLED);
        }
    }


    private User findActiveUserById(Long id) {
        if(userRepository.countById(id, EntityStatus.INACTIVE.name()) > 0){
            throw new AppException(ErrorCode.USER_DISABLED);
        }
        return userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }
}
