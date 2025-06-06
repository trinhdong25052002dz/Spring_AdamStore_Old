package Spring_AdamStore.dto.request;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewRequest {

    @NotNull(message = "Rating không được để trống")
    @Min(value = 1, message = "Rating phải lớn hơn hoặc bằng 1")
    @Max(value = 5, message = "Rating phải nhỏ hơn hoặc bằng 5")
    Double rating;
    @NotBlank(message = "comment không được để trống")
    String comment;

    String imageUrl;

    @NotNull(message = "Product ID không được để trống")
    @Min(value = 1, message = "Product ID phải lớn hơn 0")
    Long productId;
}
