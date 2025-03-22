package ApiWebManga.service.Impl;

import ApiWebManga.Entity.Roles;
import ApiWebManga.Entity.User;
import ApiWebManga.Entity.UserHasRoles;
import ApiWebManga.Enums.Role;
import ApiWebManga.Exception.NotFoundException;
import ApiWebManga.Exception.RefreshTokenExpiredException;
import ApiWebManga.dto.Request.LoginRequest;
import ApiWebManga.dto.Request.LogoutRequest;
import ApiWebManga.dto.Request.UserCreationRequest;
import ApiWebManga.dto.Response.RefreshTokenResponse;
import ApiWebManga.dto.Response.SignInResponse;
import ApiWebManga.repository.InvalidateTokenRepository;
import ApiWebManga.repository.RolesRepository;
import ApiWebManga.repository.UserRepository;
import ApiWebManga.service.AuthenticationService;
import ApiWebManga.service.EmailVerificationTokenService;
import ApiWebManga.service.JwtService;
import ApiWebManga.service.UserService;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

//làm lại jwt
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final InvalidateTokenRepository invalidateTokenRepository;

    private final EmailVerificationTokenService emailVerificationTokenService;

    private final JwtService jwtService;

    private final UserService userService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RolesRepository rolesRepository;

    private final RedisServiceImpl redisService;

    @Value("${app.secret}")
    private String appSecret;

    @Value("${app.jwt.token.expires-in}")
    private Long tokenExpiresIn;

    @Value("${app.jwt.refresh-token.expires-in}")
    private Long refreshTokenExpiresIn;
    @Override
    public User register(UserCreationRequest request){
        return userService.createUser(request);
    }
    @Transactional
    @Override
    public SignInResponse logIn(LoginRequest request, HttpServletResponse response){//thêm HttpServletResponse response vào thì có thể lưu trong cookie
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new NotFoundException("Email not found"));

        if(user.getEmailVerifiedAt()==null){
            throw new BadCredentialsException("tài khoản của ngời dùng chưa xác thực email,vui lòng xác thực email để sử dụng app");
        }

        Boolean authenticated = passwordEncoder.matches(request.getPassword(),user.getPassword());

        if(!authenticated) throw new BadCredentialsException("Password incorrect");

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        //lưu thông tin refreshToken vào repository(cái này dùng web thì tiện chứ mobile thì khó:)))
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        //lưu thông tin refresh vào cookie để nó tự động add vào cookies User và đỡ phải gửi đi mỗi khi fontend gửi request
        Cookie cookie = new Cookie("refreshToken",refreshToken);//lưu với cặp key-value
        cookie.setHttpOnly(true);//đánh dấu đây là httpOnly(bảo mật hơn)
        cookie.setSecure(false);//nếu true thì nó chỉ gửi qua HTTPS(https chỉ dùng khi deploy lên thôi)
        cookie.setDomain("localhost");
        cookie.setPath("/ApiWebManga");//chỉ được gửi truy cập khi đường dẫn abwts đầu bằng apimangaweb
        cookie.setMaxAge(24*60*60);//thời hạn tồn tại trong cookie là 1 ngày(trùng vưới thời hạn của refreshToken)

        response.addCookie(cookie);
        //trả ra các thông tin cho fontend
        return SignInResponse.builder()
                .userId(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExp(tokenExpiresIn)
                .refreshTokenExp(refreshTokenExpiresIn)
                .roleList(user.getUserHasRoles().stream().map(userHasRoles -> userHasRoles.getRole().getName()).collect(Collectors.toList()))//lưu ra 1 list role để cho fontend phân biệt
                .build();
    }

    @Override
    public void logOut(LogoutRequest request,HttpServletResponse response) throws ParseException, JOSEException {
        if (StringUtils.isBlank(request.getAccessToken())) {//import org.apache.commons.lang3.StringUtils; nhớ dùng đúng cái này để tránh bị lỗi
            throw new BadCredentialsException("Refresh token invalid");
        }
        User user = jwtService.getUserFromToken(request.getAccessToken());

        long accessTokenExp = jwtService.extractTokenExpired(request.getAccessToken());//còn bao nhiêu thời
        if(accessTokenExp > 0){
            try {
                String jwtId=jwtService.extractUserSignedJWT(request.getAccessToken()).getJWTClaimsSet().getJWTID();
                redisService.save(jwtId,request.getAccessToken(),accessTokenExp, TimeUnit.MICROSECONDS);//lưu cái accessToken hết hạn khi người dùng logout vào redis
                user.setRefreshToken(null);//xét refreshToken trong csdl=null để nó không thể gửi yêu cầu refresh accessToken nữa
                userRepository.save(user);
                //xóa refreshToken ra khỏi cookie
                Cookie cookie = new Cookie("refreshToken","");
                cookie.setHttpOnly(true);
                cookie.setSecure(true);//nếu là true thì chỉ có https mới có thể gửi request đến
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);

            }catch (Exception e) {
                log.info("không thể logout");
                throw new BadCredentialsException("Không thể logout");
            }
        }
    }

    @Override
    public RefreshTokenResponse refreshToken(String refreshToken) throws ParseException, JOSEException {
        log.info("refresh token: {}",refreshToken);
        if (StringUtils.isBlank(refreshToken)) {//import org.apache.commons.lang3.StringUtils; nhớ dùng đúng cái này để tránh bị lỗi
            throw new RefreshTokenExpiredException("Refresh token invalid 2");
        }
        String email= jwtService.extractEmail(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new NotFoundException("User Not found"));
        if(!Objects.equals(refreshToken,user.getRefreshToken())||StringUtils.isBlank(user.getRefreshToken()))
            throw new RefreshTokenExpiredException("Refresh Token Invalid");
        try {
            boolean isValidToken = jwtService.verifyToken(refreshToken);
            if (!isValidToken) {
                throw new RefreshTokenExpiredException("Refresh token invalid 2");
            }
            String accessToken = jwtService.generateAccessToken(user);
            log.info("refresh token success");
            return RefreshTokenResponse.builder()
                    .userId(user.getId())
                    .accessToken(accessToken)
                    .build();
        } catch (ParseException | JOSEException e) {
            log.error("Error while refresh token");
            throw new RefreshTokenExpiredException("Refresh token invalid");
        }

    }

    @Override
    public String verifyEmail(String token){//token ở đây là của EmailVerificationToken,chứ không phải token dùng để đăng nhập
        log.info("Verifying e-mail with token: {}",token);
        User user= emailVerificationTokenService.getUserByToken(token);
        user.setEmailVerifiedAt(LocalDateTime.now());//thười gian xác minh email
        userRepository.save(user);//chính thức là lưu user ở đây

        emailVerificationTokenService.deleteByUserId(user.getId());//xác minh xong thì xóa cái EmailVerificationToken của user này ra khỏi csdl
        log.info("E-mail verified with token: {}",token);
        return "Xác thực thành công";
    }

    public SignInResponse createAndLoginGoogle(OAuth2User oAuth2User){//đăng nhập bằng google cần cả fontend nữa nên chưa dùng được
        String email = oAuth2User.getAttribute("email");//lấy email từ trong OAuth2User của máy tính
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(()->new NotFoundException("email invalid"));
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            user.setRefreshToken(refreshToken);
            userRepository.save(user);

            return SignInResponse.builder()
                    .userId(user.getId())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .accessTokenExp(tokenExpiresIn)
                    .refreshTokenExp(refreshTokenExpiresIn)
                    .build();

        }catch (Exception e){
            Roles role = rolesRepository.findByName(String.valueOf(Role.USER))
                    .orElseThrow(()-> new NotFoundException("role nay khong ton tai"));

            User user = User.builder()
                            .email(email)
                            .phoneNumber("0379489012")
                            .fullName(oAuth2User.getAttribute("name"))
                            .password("Duong20022004@")
                            .build();
            UserHasRoles userHasRole = UserHasRoles.builder()
                    .role(role)
                    .user(user)
                    .build();
            Set<UserHasRoles> listUserHashRole =new HashSet<>();
            listUserHashRole.add(userHasRole);

            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            user.setRefreshToken(refreshToken);
            user.setUserHasRoles(listUserHashRole);

            userRepository.save(user);

            return SignInResponse.builder()
                    .userId(user.getId())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .accessTokenExp(tokenExpiresIn)
                    .refreshTokenExp(refreshTokenExpiresIn)
                    .build();
        }
    }
}