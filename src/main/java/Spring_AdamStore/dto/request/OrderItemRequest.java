package Spring_AdamStore.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemRequest {

    @Min(value = 0, message = "productVariantId phải lớn hơn hoặc bằng 0")
    @NotNull(message = "productVariantId không được null")
    Long productVariantId;
    @Min(value = 0, message = "quantity phải lớn hơn hoặc bằng 0")
    @NotNull(message = "quantity không được null")
    int quantity;
}
