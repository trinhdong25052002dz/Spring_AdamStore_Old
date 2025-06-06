package Spring_AdamStore.entity;

import Spring_AdamStore.constants.EntityStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.HashSet;
import java.util.Set;

import static Spring_AdamStore.constants.EntityStatus.ACTIVE;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Table(name = "tbl_product_variant", indexes = {
        @Index(name = "idx_product_id", columnList = "product_id"),
        @Index(name = "idx_color_id", columnList = "color_id"),
        @Index(name = "idx_size_id", columnList = "size_id")
})
@SQLDelete(sql = "UPDATE tbl_product_variant SET status = 'INACTIVE' WHERE id = ?")
@SQLRestriction("status = 'ACTIVE'")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Double price;
    Integer quantity;
    Boolean isAvailable;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "status", nullable = false)
    EntityStatus status;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;

    @ManyToOne
    @JoinColumn(name = "color_id")
    Color color;

    @ManyToOne
    @JoinColumn(name = "size_id")
    Size size;

    @OneToMany(mappedBy = "productVariant")
    Set<OrderItem> orderItems = new HashSet<>();

    @OneToMany(mappedBy = "productVariant")
     Set<CartItem> cartItems = new HashSet<>();

    @PrePersist
    public void handleBeforeCreate(){
        if(status == null){
            this.status = ACTIVE;
        }
        if(quantity > 0){
            this.isAvailable = true;
        }
    }
}
