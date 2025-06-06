package Spring_AdamStore.dto.request;

import Spring_AdamStore.constants.Gender;
import Spring_AdamStore.dto.validator.EnumPattern;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.Set;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    @NotBlank(message = "Name không được để trống")
    String name;
    @NotBlank(message = "Email không được để trống")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@gmail\\.com$", message = "Email phải có định dạng hợp lệ")
    String email;
    @Size(min = 6, message = "Password phải từ 6 kí tự trở lên")
    @NotBlank(message = "Password không được để trống")
    String password;

    Set<Long> roleIds;
}
