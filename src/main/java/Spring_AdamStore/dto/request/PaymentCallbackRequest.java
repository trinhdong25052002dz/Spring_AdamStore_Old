package Spring_AdamStore.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentCallbackRequest {
    @NotBlank(message = "Response Code không được để trống")
    String responseCode;

    @Min(value = 1, message = "orderId phải lớn hơn 0")
    @NotNull(message = "orderId không được null")
    Long orderId;
}
