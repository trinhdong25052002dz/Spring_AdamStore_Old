package Spring_AdamStore.entity.relationship;

import Spring_AdamStore.constants.EntityStatus;
import Spring_AdamStore.entity.Role;
import Spring_AdamStore.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Table(name = "tbl_user_has_role", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_role_id", columnList = "role_id")
})
@SQLRestriction("state = 'ACTIVE'")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class UserHasRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    EntityStatus state;

    @PrePersist
    public void prePersist() {
        if (state == null) {
            this.state = EntityStatus.ACTIVE;
        }
    }

    public UserHasRole(User user, Role role) {
        this.user = user;
        this.role = role;
    }

}
