package Spring_AdamStore.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Table(name = "tbl_review")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;

    @JoinColumn(nullable = false)
    @ColumnDefault(value = "5.0")
     Double rating;
    @JoinColumn(nullable = false)
     String comment;

    String imageUrl;

    @CreationTimestamp
    LocalDate createdAt;
    @UpdateTimestamp
    LocalDate updatedAt;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
     User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
     Product product;

}
