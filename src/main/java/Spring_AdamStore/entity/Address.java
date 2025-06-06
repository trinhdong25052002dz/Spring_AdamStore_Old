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
@SQLRestriction("status = 'ACTIVE'")
@SQLDelete(sql = "UPDATE tbl_address SET status = 'INACTIVE' WHERE id = ?")
@Table(name = "tbl_address")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @JoinColumn(nullable = false)
    Boolean isDefault;

    @JoinColumn(nullable = false)
    Boolean isVisible;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    EntityStatus status;

    @JoinColumn(nullable = false)
    String phone;

    @JoinColumn(nullable = false)
    String streetDetail;

    @ManyToOne
    @JoinColumn(name = "ward_id", nullable = false)
    Ward ward;

    @ManyToOne
    @JoinColumn(name = "district_id", nullable = false)
     District district;

    @ManyToOne
    @JoinColumn(name = "province_id", nullable = false)
    Province province;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
     User user;

    @OneToMany(mappedBy = "address")
    Set<Order> orders = new HashSet<>();

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
        if (isDefault == null) {
            this.isDefault = false;
        }
        if (status == null) {
            this.status = EntityStatus.ACTIVE;
        }
        if(isVisible == null){
            this.isVisible = true;
        }
    }
}
