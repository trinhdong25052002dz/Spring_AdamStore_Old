package Spring_AdamStore.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Table(name = "tbl_cart_item")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Double price;
    Integer quantity;


    @ManyToOne
    @JoinColumn(name = "product_variant_id", nullable = false)
    ProductVariant productVariant;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    Cart cart;
}
