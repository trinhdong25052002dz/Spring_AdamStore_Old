package Spring_AdamStore.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefreshRequest {
    @NotBlank(message = "refreshToken không được để trống")
    String refreshToken;


}
