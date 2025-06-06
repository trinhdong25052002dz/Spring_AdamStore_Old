package Spring_AdamStore.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Table(name = "tbl_order_item")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;

    @JoinColumn(nullable = false)
    @ColumnDefault(value = "0")
     Integer quantity;
    @JoinColumn(nullable = false)
    @ColumnDefault(value = "0")
     Double unitPrice;


    @ManyToOne
    @JoinColumn(name = "product_variant_id", nullable = false)
    ProductVariant productVariant;

    @ManyToOne
    @JoinColumn(name = "order_id")
     Order order;
}
