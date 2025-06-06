package Spring_AdamStore.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageResponse<T> {

     int page;
     int size;
     long totalPages;
     long totalItems;
     List<T> items;
}