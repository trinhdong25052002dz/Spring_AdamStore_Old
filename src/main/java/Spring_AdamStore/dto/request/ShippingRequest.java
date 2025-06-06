package Spring_AdamStore.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShippingRequest {

    @Min(value = 0, message = "addressId phải lớn hơn hoặc bằng 0")
    @NotNull(message = "addressId không được null")
    Long addressId;

    @NotEmpty(message = "orderItems không được để trống")
    List<OrderItemRequest> orderItems;
}
