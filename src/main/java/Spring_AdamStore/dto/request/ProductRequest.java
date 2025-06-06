package Spring_AdamStore.dto.request;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {

    @NotBlank(message = "Tên sản phẩm không được để trống")
    String name;
    @NotBlank(message = "Mô tả sản phẩm không được để trống")
    String description;
    @Min(value = 0, message = "Giá phải lớn hơn hoặc bằng 0")
    @NotNull(message = "price không được null")
    Double price;
    @Min(value = 0, message = "Giá phải lớn hơn hoặc bằng 0")
    @NotNull(message = "quantity không được null")
    Integer quantity;

    @NotNull(message = "categoryId không được null")
    Long categoryId;

    @NotEmpty(message = "colors không được để trống")
    Set<Long> colorIds;

    @NotEmpty(message = "sizes không được để trống")
    Set<Long> sizeIds;

    Set<Long> imageIds;

}
