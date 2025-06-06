package Spring_AdamStore.dto.response;

import Spring_AdamStore.constants.EntityStatus;
import Spring_AdamStore.constants.Gender;
import Spring_AdamStore.dto.basic.EntityBasic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.LocalDate;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    Long id;
    String name;
    String email;
    @Enumerated(EnumType.STRING)
    EntityStatus status;
    String avatarUrl;
    LocalDate dob;
    @Enumerated(EnumType.STRING)
    Gender gender;

    String createdBy;
    String updatedBy;
    LocalDate createdAt;
    LocalDate updatedAt;

    Set<EntityBasic> roles;
}
