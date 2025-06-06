package Spring_AdamStore.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RevenueByMonthDTO {

     Object month;
     Double totalAmount;
}
