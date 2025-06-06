package Spring_AdamStore.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRevenueDTO {

     Long orderId;
     String customerName;
     LocalDate orderDate;
     Double totalAmount;
}
