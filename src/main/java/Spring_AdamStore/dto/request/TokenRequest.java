package Spring_AdamStore.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenRequest {
    @NotBlank(message = "accessToken không được để trống")
    String accessToken;
}
