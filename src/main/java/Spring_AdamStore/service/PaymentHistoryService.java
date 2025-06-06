package Spring_AdamStore.service;

import Spring_AdamStore.constants.PaymentMethod;
import Spring_AdamStore.constants.PaymentStatus;
import Spring_AdamStore.dto.response.PageResponse;
import Spring_AdamStore.dto.response.PaymentHistoryResponse;
import Spring_AdamStore.entity.Order;
import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentHistoryService {

    void savePaymentHistory(Order order, PaymentMethod method);

    PageResponse<PaymentHistoryResponse> searchPaymentHistories(int pageNo, int pageSize, String sortBy,
                                                                LocalDateTime startDate, LocalDateTime endDate,
                                                                PaymentStatus paymentStatus);

    void delete(Long id);

}
