package Spring_AdamStore.dto.response;

import Spring_AdamStore.dto.basic.EntityBasic;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Set;


@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenResponse {

    String accessToken;
    String refreshToken;
    boolean authenticated;
    String email;

    Set<EntityBasic> roles;

}
