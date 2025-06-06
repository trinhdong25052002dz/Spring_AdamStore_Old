package Spring_AdamStore.entity;

import Spring_AdamStore.constants.EntityStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_category")
@SQLRestriction("status = 'ACTIVE'")
@SQLDelete(sql = "UPDATE tbl_category SET status = 'INACTIVE' WHERE id = ?")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    String name;
    @Column(columnDefinition = "MEDIUMTEXT")
    String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    EntityStatus status;


    @CreatedBy
    String createdBy;
    @LastModifiedBy
    String updatedBy;
    @CreationTimestamp
    LocalDate createdAt;
    @UpdateTimestamp
    LocalDate updatedAt;

    @OneToMany(mappedBy = "category")
    Set<Product> products = new HashSet<>();

    @PrePersist
    public void handleBeforeCreate() {
        if (status == null) {
            this.status = EntityStatus.ACTIVE;
        }
    }

}
