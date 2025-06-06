package Spring_AdamStore.dto.basic;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductVariantBasic {

    Long id;

    EntityBasic color;
    EntityBasic size;
    EntityBasic product;
}
