package ApiWebManga.service;

import ApiWebManga.Entity.User;
import ApiWebManga.dto.Request.IntrospectRequest;
import ApiWebManga.dto.Request.LoginRequest;
import ApiWebManga.dto.Request.LogoutRequest;
import ApiWebManga.dto.Request.UserCreationRequest;
import ApiWebManga.dto.Response.IntrospectResponse;
import ApiWebManga.dto.Response.RefreshTokenResponse;
import ApiWebManga.dto.Response.SignInResponse;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CookieValue;

import java.text.ParseException;

public interface AuthenticationService {
    User register(UserCreationRequest request);
    SignInResponse logIn(LoginRequest request, HttpServletResponse response);
    void logOut(LogoutRequest request,HttpServletResponse response) throws ParseException, JOSEException;
    RefreshTokenResponse refreshToken(@CookieValue(name = "refreshToken") String refreshToken) throws ParseException, JOSEException;
    String verifyEmail(String token);
    IntrospectResponse introspect(IntrospectRequest request);
    SignInResponse loginWithGoogle(String code,HttpServletResponse response);
}
