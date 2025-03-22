package ApiWebManga.service;

import ApiWebManga.Entity.User;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;

public interface JwtService {
    String generateAccessToken(User user);
    String generateRefreshToken(User user);
    SignedJWT extractUserSignedJWT(String accessToken) throws ParseException, JOSEException;
    boolean verifyToken(String token) throws ParseException, JOSEException;
    boolean verificationToken(String token,User user);
    Long extractTokenExpired(String token);
    boolean isTokenExpired(String token) throws ParseException, JOSEException;
    User getUserFromToken(String token);
    String extractEmail(String token) throws ParseException, JOSEException;
}
