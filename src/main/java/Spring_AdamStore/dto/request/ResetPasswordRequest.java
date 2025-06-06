package Spring_AdamStore.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResetPasswordRequest {
    @NotBlank(message = "forgotPasswordToken không được để trống")
    String forgotPasswordToken;
    @Size(min = 6, message = "Password phải từ 6 kí tự trở lên")
    @NotBlank(message = "Password không được để trống")
    String newPassword;
    @Size(min = 6, message = "confirmPassword phải từ 6 kí tự trở lên")
    @NotBlank(message = "confirmPassword không được để trống")
    String confirmPassword;
}
