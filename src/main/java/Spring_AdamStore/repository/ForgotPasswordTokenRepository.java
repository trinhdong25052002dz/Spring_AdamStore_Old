package Spring_AdamStore.repository;

import Spring_AdamStore.entity.ForgotPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForgotPasswordTokenRepository extends JpaRepository<ForgotPasswordToken, Long> {

    Optional<ForgotPasswordToken> findByForgotPasswordToken(String forgotPasswordToken);

}
