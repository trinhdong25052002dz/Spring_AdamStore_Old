package Spring_AdamStore.repository;

import Spring_AdamStore.entity.Address;
import Spring_AdamStore.entity.Branch;
import Spring_AdamStore.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findAllByUser(User user);

    Page<Address> findAllByUserIdAndIsVisible(Long userId, Boolean isVisible, Pageable pageable);

    @Query(value = "SELECT * FROM tbl_address a WHERE a.id = :id", nativeQuery = true)
    Optional<Address> findAddressById(@Param("id") Long id);

    @Query(value = "SELECT * FROM tbl_address",
            countQuery = "SELECT COUNT(*) FROM tbl_address",
            nativeQuery = true)
    Page<Address> findAllAddresses(Pageable pageable);
}
