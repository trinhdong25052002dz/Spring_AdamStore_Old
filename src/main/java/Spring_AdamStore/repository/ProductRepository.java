package Spring_AdamStore.repository;

import Spring_AdamStore.entity.Branch;
import Spring_AdamStore.entity.District;
import Spring_AdamStore.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT COUNT(*) FROM tbl_product WHERE name = :name", nativeQuery = true)
    Long countByName(@Param("name") String name);

    @Query(value = "SELECT * FROM tbl_product p WHERE p.id = :id", nativeQuery = true)
    Optional<Product> findProductById(@Param("id") Long id);

    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    @Query(value = "SELECT * FROM tbl_product p WHERE p.category_id = :categoryId", nativeQuery = true)
    Page<Product> findAllByCategoryId(Long categoryId, Pageable pageable);


    @Query(value = "SELECT COUNT(*) FROM tbl_product p WHERE p.category_id = :categoryId AND p.status = :status", nativeQuery = true)
    Long countActiveProductsByCategoryId(@Param("categoryId") Long categoryId, @Param("status") String status);

    @Query(value = "SELECT * FROM tbl_product",
            countQuery = "SELECT COUNT(*) FROM tbl_product",
            nativeQuery = true)
    Page<Product> findAllProducts(Pageable pageable);
}
