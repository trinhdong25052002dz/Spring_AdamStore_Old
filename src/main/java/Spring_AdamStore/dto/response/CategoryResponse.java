package Spring_AdamStore.dto.response;

import Spring_AdamStore.constants.EntityStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {

    Long id;
    String name;
    String description;
    @Enumerated(EnumType.STRING)
    EntityStatus status;
    
    String createdBy;
    String updatedBy;
    LocalDate createdAt;
    LocalDate updatedAt;
}
