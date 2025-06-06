package Spring_AdamStore.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BranchUpdateRequest {

    String name;
    String location;
    @Pattern(regexp = "^0[35789]\\d{8}$", message = "Số điện thoại phải có 10 số và bắt đầu bằng 0")
    String phone;
}
