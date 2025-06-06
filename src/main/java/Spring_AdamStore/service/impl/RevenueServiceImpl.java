package Spring_AdamStore.service.impl;

import Spring_AdamStore.dto.response.OrderRevenueDTO;
import Spring_AdamStore.dto.response.PageResponse;
import Spring_AdamStore.dto.response.RevenueByMonthDTO;
import Spring_AdamStore.dto.response.UserResponse;
import Spring_AdamStore.entity.Order;
import Spring_AdamStore.entity.User;
import Spring_AdamStore.repository.PaymentHistoryRepository;
import Spring_AdamStore.service.PageableService;
import Spring_AdamStore.service.RevenueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j(topic = "REVENUE-SERVICE")
@RequiredArgsConstructor
@Service
public class RevenueServiceImpl implements RevenueService {

    private final PaymentHistoryRepository paymentHistoryRepository;
    private final PageableService pageableService;

    @Override
    public List<RevenueByMonthDTO> getRevenueByMonth(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        return paymentHistoryRepository.getRevenueByMonth(startDateTime, endDateTime);
    }

    @Override
    public PageResponse<OrderRevenueDTO> getOrderRevenueByDate(int pageNo, int pageSize, String sortBy, LocalDate startDate, LocalDate endDate) {
        pageNo = pageNo - 1;

        Pageable pageable = pageableService.createPageable(pageNo, pageSize, sortBy, Order.class);

        Page<OrderRevenueDTO> orderRevenueDTOPage = paymentHistoryRepository
                .getRevenueOrdersByDate(startDate, endDate, pageable);

        return PageResponse.<OrderRevenueDTO>builder()
                .page(orderRevenueDTOPage.getNumber() + 1)
                .size(orderRevenueDTOPage.getSize())
                .totalPages(orderRevenueDTOPage.getTotalPages())
                .totalItems(orderRevenueDTOPage.getTotalElements())
                .items(orderRevenueDTOPage.getContent())
                .build();
    }

}
