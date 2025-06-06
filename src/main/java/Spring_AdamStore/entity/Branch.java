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

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("status = 'ACTIVE'")
@SQLDelete(sql = "UPDATE tbl_branch SET status = 'INACTIVE' WHERE id = ?")
@Table(name = "tbl_branch")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;

    @JoinColumn(nullable = false, unique = true)
     String name;
    @JoinColumn(nullable = false)
     String location;
    @JoinColumn(nullable = false, unique = true)
     String phone;

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


    @PrePersist
    public void handleBeforeCreate() {
        if (status == null) {
            this.status = EntityStatus.ACTIVE;
        }
    }
}
