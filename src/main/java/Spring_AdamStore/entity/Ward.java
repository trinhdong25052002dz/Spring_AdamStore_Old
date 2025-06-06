package Spring_AdamStore.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Table(name = "tbl_ward", indexes =
    @Index(name = "idx_district_id", columnList = "district_id")
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Ward {
    @Id
    String code;

    String name;

    @ManyToOne
    @JoinColumn(name = "district_id")
    District district;
}
