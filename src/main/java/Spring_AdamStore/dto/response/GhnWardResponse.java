package Spring_AdamStore.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GhnWardResponse {

     Integer code;
     String message;
     List<GhnWard> data;
}
