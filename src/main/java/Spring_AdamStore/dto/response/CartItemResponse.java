package Spring_AdamStore.dto.response;

import Spring_AdamStore.dto.basic.ProductVariantBasic;
import jakarta.persistence.JoinColumn;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {

    Long id;

    Double price;
    Integer quantity;

    ProductVariantBasic productVariantBasic;
}
