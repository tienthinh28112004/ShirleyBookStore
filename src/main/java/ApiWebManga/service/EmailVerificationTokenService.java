package ApiWebManga.service;

import ApiWebManga.Entity.EmailVerificationToken;
import ApiWebManga.Entity.User;

public interface EmailVerificationTokenService{
    EmailVerificationToken create(User user);
    User getUserByToken(String token);
    void deleteByUserId(Long userId);
}
