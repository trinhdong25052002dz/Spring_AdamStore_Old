package Spring_AdamStore.dto.response;

import Spring_AdamStore.constants.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentHistoryResponse {

    Long id;
    Boolean isPrimary;

    String paymentMethod;
    Double totalAmount;
    PaymentStatus paymentStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime paymentTime;
    String note;
}
