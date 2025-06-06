package Spring_AdamStore.dto.response;

import Spring_AdamStore.dto.basic.ProductVariantBasic;
import lombok.*;
import lombok.experimental.FieldDefaults;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {

    Long id;

    Double unitPrice;
    Integer quantity;

    ProductVariantBasic productVariant;
}
