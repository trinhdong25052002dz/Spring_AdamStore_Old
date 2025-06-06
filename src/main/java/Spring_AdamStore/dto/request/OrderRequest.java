package Spring_AdamStore.dto.request;

import Spring_AdamStore.constants.OrderStatus;
import Spring_AdamStore.constants.PaymentMethod;
import Spring_AdamStore.dto.validator.EnumPattern;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {

    @Min(value = 0, message = "addressId phải lớn hơn hoặc bằng 0")
    @NotNull(message = "addressId không được null")
    Long addressId;

    @NotEmpty(message = "orderItems không được để trống")
    List<OrderItemRequest> orderItems;

    Long promotionId;

    @Min(value = 0, message = "shippingFee phải lớn hơn hoặc bằng 0")
    @NotNull(message = "shippingFee không được null")
    Integer shippingFee;

    @NotNull(message = "paymentMethod không được để trống")
    @EnumPattern(name = "paymentMethod", regexp = "VNPAY|CASH")
    PaymentMethod paymentMethod;
}
