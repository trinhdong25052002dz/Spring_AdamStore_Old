package Spring_AdamStore.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Table(name = "tbl_district", indexes = {
        @Index(name = "idx_province_id", columnList = "province_id")
})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class District {
    @Id
    Integer id;

    @JoinColumn(nullable = false)
     String name;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id")
     Province province;
}
