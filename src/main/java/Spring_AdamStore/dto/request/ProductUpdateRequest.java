package Spring_AdamStore.dto.request;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdateRequest {

    String name;
    String description;
    @Min(value = 0, message = "Giá phải lớn hơn hoặc bằng 0")
    @NotNull(message = "price không được null")
    Double price;
    @Min(value = 0, message = "Giá phải lớn hơn hoặc bằng 0")
    @NotNull(message = "quantity không được null")
    Integer quantity;

    Long categoryId;

    Set<Long> colorIds;

    Set<Long> sizeIds;

    Set<Long> imageIds;

}
