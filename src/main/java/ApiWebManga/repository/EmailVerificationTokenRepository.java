package ApiWebManga.repository;

import ApiWebManga.Entity.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken,Long> {
    EmailVerificationToken findByUserId(Long id);
    Optional<EmailVerificationToken> findByToken(String token);

    @Modifying//đánh dấu đây không phải là select
    @Query("DELETE FROM EmailVerificationToken vr WHERE vr.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}
