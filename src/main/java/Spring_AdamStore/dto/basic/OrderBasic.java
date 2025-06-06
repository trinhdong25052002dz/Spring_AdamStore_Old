package Spring_AdamStore.dto.basic;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderBasic {

    Long id;
    LocalDate orderDate;

    String username;
    String email;
}
