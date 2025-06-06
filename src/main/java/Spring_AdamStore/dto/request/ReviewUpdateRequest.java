package Spring_AdamStore.dto.request;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewUpdateRequest {

    Double rating;
    String comment;
    String imageUrl;


}
