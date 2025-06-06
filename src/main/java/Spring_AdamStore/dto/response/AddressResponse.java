package Spring_AdamStore.dto.response;

import Spring_AdamStore.constants.EntityStatus;
import Spring_AdamStore.dto.basic.EntityBasic;
import Spring_AdamStore.dto.basic.UserBasic;
import Spring_AdamStore.dto.basic.WardBasic;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {

    Long id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    String userName;
    @Enumerated(EnumType.STRING)
    EntityStatus status;

    Boolean isVisible;


    Boolean isDefault;

    String phone;
    String streetDetail;

    WardBasic ward;

    EntityBasic district;

    EntityBasic province;


}
