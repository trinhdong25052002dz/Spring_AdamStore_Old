package Spring_AdamStore.repository.relationship;

import Spring_AdamStore.entity.Role;
import Spring_AdamStore.entity.User;
import Spring_AdamStore.entity.relationship.UserHasRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserHasRoleRepository extends JpaRepository<UserHasRole,Long> {

    void deleteByUser(User user);

    UserHasRole findUserHasRoleByUserAndRole (User user, Role role);

}
