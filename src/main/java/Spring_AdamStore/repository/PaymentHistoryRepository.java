package Spring_AdamStore.repository;

import Spring_AdamStore.constants.PaymentMethod;
import Spring_AdamStore.constants.PaymentStatus;
import Spring_AdamStore.dto.response.OrderRevenueDTO;
import Spring_AdamStore.dto.response.RevenueByMonthDTO;
import Spring_AdamStore.entity.Order;
import Spring_AdamStore.entity.PaymentHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {

    Optional<PaymentHistory> findByOrderIdAndPaymentStatusAndPaymentMethod(Long orderId, PaymentStatus paymentStatus, PaymentMethod paymentMethod);


    @Query("SELECT p FROM PaymentHistory p " +
            "WHERE (:paymentStatus IS NULL OR p.paymentStatus = :paymentStatus) " +
            "AND (:startDate IS NULL OR p.paymentTime >= :startDate) " +
            "AND (:endDate IS NULL OR p.paymentTime <= :endDate)")
    Page<PaymentHistory> searchPaymentHistories(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate,
            @Param("paymentStatus") PaymentStatus paymentStatus, Pageable pageable);


    @Query("""
    SELECT new Spring_AdamStore.dto.response.RevenueByMonthDTO(
        DATE_FORMAT(ph.paymentTime, '%Y-%m'),
        SUM(ph.totalAmount)
    )
    FROM PaymentHistory ph
    WHERE ph.isPrimary = true
      AND ph.paymentStatus = 'PAID'
      AND ph.paymentTime >= :startDate AND ph.paymentTime <= :endDate
    GROUP BY DATE_FORMAT(ph.paymentTime, '%Y-%m')
    ORDER BY DATE_FORMAT(ph.paymentTime, '%Y-%m')
""")
    List<RevenueByMonthDTO> getRevenueByMonth(@Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate);


    @Query("""
        SELECT new Spring_AdamStore.dto.response.OrderRevenueDTO(
            o.id,
            u.email,
            o.orderDate,
            o.totalPrice
        )
        FROM Order o
        JOIN o.user u
        JOIN o.payments ph
        WHERE ph.isPrimary = true
          AND ph.paymentStatus = 'PAID'
          AND o.orderDate BETWEEN :startDate AND :endDate
        ORDER BY o.orderDate
    """)
    Page<OrderRevenueDTO> getRevenueOrdersByDate(@Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate,
                                                 Pageable pageable);

}
