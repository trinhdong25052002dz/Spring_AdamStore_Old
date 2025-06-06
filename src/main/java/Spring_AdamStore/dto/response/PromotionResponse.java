package Spring_AdamStore.dto.response;

import Spring_AdamStore.constants.EntityStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PromotionResponse {

    Long id;

    String code;
    String description;
    Integer discountPercent;
    LocalDate startDate;
    LocalDate endDate;
    @Enumerated(EnumType.STRING)
    EntityStatus status;

    String createdBy;
    String updatedBy;
    LocalDate createdAt;
    LocalDate updatedAt;
}
