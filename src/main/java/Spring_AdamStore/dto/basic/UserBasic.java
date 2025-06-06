package Spring_AdamStore.dto.basic;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserBasic {

    Long id;
    String name;
    String email;
    String phone;
}
