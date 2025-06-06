package Spring_AdamStore.entity.relationship;

import Spring_AdamStore.constants.EntityStatus;
import Spring_AdamStore.entity.Order;
import Spring_AdamStore.entity.Promotion;
import Spring_AdamStore.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Table(name = "tbl_promotion_usage")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class PromotionUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Double discountAmount;
    LocalDateTime usedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "promotion_id", nullable = false)
    Promotion promotion;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    Order order;
}
