package ApiWebManga.Controller;


import ApiWebManga.Entity.User;
import ApiWebManga.Utils.LocalizationUtils;
import ApiWebManga.dto.Request.*;
import ApiWebManga.dto.Response.RefreshTokenResponse;
import ApiWebManga.dto.Response.SignInResponse;
import ApiWebManga.service.Impl.AuthenticationServiceImpl;
import com.nimbusds.jose.JOSEException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class AuthenticationController {

    private final AuthenticationServiceImpl authenticationService;
    private final LocalizationUtils localizationUtils;
    @PostMapping("/auth/register")
    @Operation( summary = "Register endpoint")
    public ApiResponse<User> register(@RequestBody @Valid UserCreationRequest request){
        //Locale locale = RequestContextUtils.getLocale(httpServletRequest);
        return ApiResponse.<User>builder()
                .message(localizationUtils.getLocalizedMessage("registered_successfully"))
                .result(authenticationService.register(request))
                .build();
    }

    @GetMapping("/auth/email-verification/{token}")//token ở đây là token của emailVerifyToken chứ không phải của user đăng nhập
    @Operation(summary = "E-mail verification endpoint")
    public ApiResponse<String> emailVerification(@PathVariable("token") String token){
        return ApiResponse.<String>builder()
                .message("Email verification successfully")
                .result(authenticationService.verifyEmail(token))
                .build();
    }


    @PostMapping("/auth/login")
    @Operation(summary = "Login endpoint")
    public ApiResponse<SignInResponse> login(
            @Parameter(description = "Request body to login", required = true)
            @RequestBody @Valid LoginRequest loginRequest,HttpServletResponse response){
        return ApiResponse.<SignInResponse>builder()
                .message("login")
                .result(authenticationService.logIn(loginRequest,response))
                .build();
    }

    @PostMapping("/auth/logout")
    @Operation(summary = "Logout endpoint")
    public ApiResponse<String> logout( @RequestBody @Valid LogoutRequest request,HttpServletResponse response) throws ParseException, JOSEException {
        authenticationService.logOut(request,response);
        return ApiResponse.<String>builder()
                .message("Logout thành công thành công")
                .build();
    }

    @PostMapping("/refresh")
    @Operation(summary = "refresh endpoint")
    public ApiResponse<RefreshTokenResponse> refresh(
            @CookieValue(name = "refreshToken", required = false) String refreshToken) throws ParseException, JOSEException {
        log.info("refresh token");
        return ApiResponse.<RefreshTokenResponse>builder()
                .result(authenticationService.refreshToken(refreshToken))
                .build();
    }

    @GetMapping("/auth/google-login")
    public void googleLogin(HttpServletResponse response) throws IOException{
        var authentiaction = SecurityContextHolder.getContext().getAuthentication();
        OAuth2User user = (OAuth2User) authentiaction.getPrincipal();//lấy thông tin đang đăng nhập hiện tại

        SignInResponse sign= authenticationService.createAndLoginGoogle(user);
        String redirectUrl = "http://localhost:8080/apiMangaWeb/token="+sign.getAccessToken();
        response.sendRedirect(redirectUrl);//dùng trong localhost thì được chứ sau có domain thì phải dùng kiểu khác
    }

//    @PostMapping("/sendOTP")
//    public String sendOTP(@RequestBody String email){
//        try {
//            //return mailSenderService.sendEmailUser(email);
//        } catch (MessagingException e) {
//            e.printStackTrace();//dùng để debug lỗi
//            return "Failed to send OTP.";
//        }
//    }
}
