package Spring_AdamStore.repository;

import Spring_AdamStore.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {

    Set<FileEntity> findAllByIdIn(Set<Long> imageSet);

}
