package Spring_AdamStore.entity;

import Spring_AdamStore.constants.PaymentMethod;
import Spring_AdamStore.constants.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Table(name = "tbl_payment_history")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PaymentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;

    Boolean isPrimary;

    @Enumerated(EnumType.STRING)
    @JoinColumn(nullable = false)
    PaymentMethod paymentMethod;

     @JoinColumn(nullable = false)
     Double totalAmount;

    @Enumerated(EnumType.STRING)
    @JoinColumn(nullable = false)
     PaymentStatus paymentStatus;

     LocalDateTime paymentTime;

    @ManyToOne
    @JoinColumn(name = "order_id")
     Order order;

    @PrePersist
    public void handleBeforeCreate() {
        if(isPrimary == null){
            this.isPrimary = false;
        }
    }
}
