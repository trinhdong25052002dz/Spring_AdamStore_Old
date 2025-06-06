package Spring_AdamStore.dto.response;


import Spring_AdamStore.constants.EntityStatus;
import Spring_AdamStore.dto.basic.EntityBasic;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    Long id;
    String name;
    String description;

    Boolean isAvailable;
    Double averageRating;
    Integer soldQuantity;
    Integer totalReviews;
    Integer quantity;
    Double price;

    @Enumerated(EnumType.STRING)
    EntityStatus status;

    String createdBy;
    String updatedBy;
    LocalDate createdAt;
    LocalDate updatedAt;

    EntityBasic category;

    Set<EntityBasic> colors;
    Set<EntityBasic> sizes;

    Set<FileResponse> images;


}
