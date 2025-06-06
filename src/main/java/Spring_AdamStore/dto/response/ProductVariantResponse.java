package Spring_AdamStore.dto.response;

import Spring_AdamStore.constants.EntityStatus;
import Spring_AdamStore.dto.basic.EntityBasic;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantResponse {

    Long id;

    Double price;
    Integer quantity;
    Boolean isAvailable;
    @Enumerated(EnumType.STRING)
    EntityStatus status;

    EntityBasic product;
    EntityBasic size;
    EntityBasic color;
}
