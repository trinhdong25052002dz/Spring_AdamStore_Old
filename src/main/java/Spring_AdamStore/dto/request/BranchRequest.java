package Spring_AdamStore.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BranchRequest {

    @NotBlank(message = "name không được để trống")
    String name;
    @NotBlank(message = "location không được để trống")
    String location;
    @Pattern(regexp = "^0[35789]\\d{8}$", message = "Số điện thoại phải có 10 số và bắt đầu bằng 0")
    @NotBlank(message = "phone không được để trống")
    String phone;
}
