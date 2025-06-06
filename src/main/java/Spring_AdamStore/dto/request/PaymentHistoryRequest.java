package Spring_AdamStore.dto.request;

import Spring_AdamStore.constants.PaymentStatus;
import Spring_AdamStore.dto.validator.EnumPattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentHistoryRequest {

    String paymentMethod;
    Double totalAmount;
    @EnumPattern(name = "paymentStatus", regexp = "PAID|REFUNDED|CANCELED")
    PaymentStatus paymentStatus;
    String note;


}
