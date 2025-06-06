package Spring_AdamStore.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Table(name = "tbl_province")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Province {
    @Id
    Integer id;
    String name;

    @OneToMany(mappedBy = "province", fetch = FetchType.LAZY)
    Set<District> districts = new HashSet<>();


}
