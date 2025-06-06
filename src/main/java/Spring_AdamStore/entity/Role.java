package Spring_AdamStore.entity;

import Spring_AdamStore.entity.relationship.RoleHasPermission;
import Spring_AdamStore.entity.relationship.UserHasRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "tbl_role")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    String name;
    String description;

    @OneToMany(mappedBy = "role")
    @JsonIgnore
    Set<UserHasRole> users = new HashSet<>();

    @OneToMany(mappedBy = "role")
    Set<RoleHasPermission> permissions = new HashSet<>();
}
