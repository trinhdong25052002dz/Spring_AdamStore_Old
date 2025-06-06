package Spring_AdamStore.entity;

import Spring_AdamStore.constants.EntityStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static Spring_AdamStore.constants.EntityStatus.ACTIVE;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Table(name = "tbl_product")
@SQLDelete(sql = "UPDATE tbl_product SET status = 'INACTIVE' WHERE id = ?")
@SQLRestriction("status = 'ACTIVE'")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @JoinColumn(nullable = false, unique = true)
    String name;
    @Column(columnDefinition = "MEDIUMTEXT")
    String description;
    Boolean isAvailable;
    @ColumnDefault(value = "0")
    Integer soldQuantity;
    @ColumnDefault(value = "5.0")
    Double averageRating;
    @ColumnDefault("0")
    Integer totalReviews;

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


    @ManyToOne
    @JoinColumn(name = "category_id")
     Category category;

    @OneToMany(mappedBy = "product")
    Set<Review> reviews = new HashSet<>();

    @OneToMany(mappedBy = "product")
    Set<FileEntity> images = new HashSet<>();

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    Set<ProductVariant> productVariants = new HashSet<>();

    @PrePersist
    public void handleBeforeCreate() {
        if(status == null){
            this.status = ACTIVE;
        }
        if(soldQuantity == null){
            this.soldQuantity = 0;
        }
        if(averageRating == null){
            this.averageRating = 5.0;
        }
        if(totalReviews == null){
            this.totalReviews = 0;
        }
        if(isAvailable == null){
            this.isAvailable = true;
        }
    }
}
