package ApiWebManga.service;

import ApiWebManga.Entity.EmailVerificationToken;
import ApiWebManga.Entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface EmailVerificationTokenService{
    EmailVerificationToken create(User user);
    User getUserByToken(String token);

    @Modifying
    @Query("DELETE FROM EmailVerificationToken rt WHERE rt.user.id = :userId")
    void deleteByUserId(Long userId);
}
