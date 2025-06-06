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
public class GhnDistrictResponse {

     int code;
     String message;
     List<GhnDistrict> data;

}
