package Spring_AdamStore.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemRequest {

    @Min(value = 1, message = "productVariantId phải lớn hơn 0")
    @NotNull(message = "productVariantId không được null")
    Long productVariantId;

    @Min(value = 1, message = "quantity phải lớn hơn 0")
    @NotNull(message = "quantity không được null")
    Integer quantity;

}
