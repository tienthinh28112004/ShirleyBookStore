package ApiWebManga.service.Impl;

import ApiWebManga.Entity.Roles;
import ApiWebManga.Entity.User;
import ApiWebManga.Entity.UserHasRoles;
import ApiWebManga.Enums.Role;
import ApiWebManga.Exception.NotFoundException;
import ApiWebManga.Exception.RefreshTokenExpiredException;
import ApiWebManga.dto.Request.*;
import ApiWebManga.dto.Response.*;
import ApiWebManga.repository.HttpClient.OutboundIdentityClient;
import ApiWebManga.repository.HttpClient.OutboundUserClient;
import ApiWebManga.repository.InvalidateTokenRepository;
import ApiWebManga.repository.RolesRepository;
import ApiWebManga.repository.UserRepository;
import ApiWebManga.service.AuthenticationService;
import ApiWebManga.service.EmailVerificationTokenService;
import ApiWebManga.service.JwtService;
import ApiWebManga.service.UserService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;
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

    private final OutboundIdentityClient outboundIdentityClient;

    private final OutboundUserClient outboundUserClient;

    @Value("${app.secret}")
    private String appSecret;

    @Value("${app.jwt.token.expires-in}")
    private Long tokenExpiresIn;

    @Value("${app.jwt.refresh-token.expires-in}")
    private Long refreshTokenExpiresIn;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;
    private final String grant_type = "authorization_code";
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

    public IntrospectResponse introspect(IntrospectRequest request) {
        String token = request.getAccessToken();

        User user = jwtService.getUserFromToken(token);

        boolean isValid = true;
        List<String> scope=new ArrayList<>();
        try{
            boolean verify = jwtService.verificationToken(token,user);
            SignedJWT signedJWT=jwtService.extractUserSignedJWT(token);
            List<String> authority= (List<String>) SignedJWT.parse(token).getJWTClaimsSet().getClaim("Authority");
            scope.addAll(authority);
            log.info("arrayLisst ={}",scope);
        }catch (Exception e){
            isValid=false;
        }
        log.info("isValid {}",isValid);
        return IntrospectResponse.builder()
                .valid(isValid)
                .scope(scope)
                .build();
    }

    @Override
    @Transactional
    public String verifyEmail(String token){//token ở đây là của EmailVerificationToken,chứ không phải token dùng để đăng nhập
        log.info("Verifying e-mail with token: {}",token);
        User user= emailVerificationTokenService.getUserByToken(token);
        user.setEmailVerifiedAt(LocalDateTime.now());//thười gian xác minh email
        user.setActive(true);//xác thực tạo user thành công
        userRepository.save(user);//chính thức là lưu user ở đây

        emailVerificationTokenService.deleteByUserId(user.getId());//xác minh xong thì xóa cái EmailVerificationToken của user này ra khỏi csdl
        log.info("E-mail verified with token: {}",token);
        return "Xác thực thành công";
    }
    @Transactional
    public SignInResponse loginWithGoogle(String code,HttpServletResponse response){//nhận lại code từu fontend truyền lên
        ExchangeTokenResponse result = outboundIdentityClient.exchangeToken(ExchangeTokenRequest.builder()
                        .code(code)//code này là do google trả về có hiệu lúc trong vài phút và dùng để lấy accessToken từ google
                        .clientId(clientId)
                        .clientSecret(clientSecret)//dù có code nhưng fontend không thể tự giải mã được mà phải nhờ backend do backend có clientSecret
                        .redirectUri(redirectUri)//đường dẫn của fontend gửi lên request này
                        .grantType(grant_type)
                .build());
        //result ở đây có các giá trị khác nhau nhưng chỉ cần quan tâm đến accessToken mà google trả về thôi(dổi nó lấy thông tin user để dùng)
        OutboundUserResponse userInfo = outboundUserClient.getUserInfo("json", result.getAccessToken());//lấy dữ liệu dạng json và truyền vào accessToken lấy được từ google

        Roles roles = rolesRepository.findByName(String.valueOf(Role.USER))
                .orElseThrow(()->new NotFoundException("Role not found"));
        //nếu tồn tại user rồi thì thôi còn không thì tạo mới
        User user = userRepository.findByEmail(userInfo.getEmail()).orElseGet(()->userRepository.save(User.builder()
                        .email(userInfo.getEmail())
                        .fullName(userInfo.getName())
                        .avatarUrl(userInfo.getPicture())
                        .isActive(userInfo.isVerified())
                        .emailVerifiedAt(userInfo.isVerified()?LocalDateTime.now():null)
                .build()));

        Set<UserHasRoles> rolesList = new HashSet<>();
        UserHasRoles userHasRole=UserHasRoles.builder()
                .role(roles)
                .user(user)
                .build();
        rolesList.add(userHasRole);
        user.setUserHasRoles(rolesList);

        String accessToken= jwtService.generateAccessToken(user);
        String refreshToken= jwtService.generateRefreshToken(user);

        user.setRefreshToken(refreshToken);

        Cookie cookie=new Cookie("refreshToken",refreshToken);
        cookie.setMaxAge(24*60*60);//thời hạn 1 ngày
        cookie.setHttpOnly(true);
        cookie.setSecure(false);//nếu true thì chỉ có https mới thông qua thôi
        cookie.setDomain("localhost");
        cookie.setPath("/ApiWebManga");

        response.addCookie(cookie);
        return SignInResponse.builder()
                .userId(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExp(tokenExpiresIn)
                .refreshTokenExp(refreshTokenExpiresIn)
                .roleList(user.getUserHasRoles().stream().map(userHasRoles -> userHasRoles.getRole().getName()).collect(Collectors.toList()))//lưu ra 1 list role để cho fontend phân biệt
                .build();
    }
//1,Frontend redirect người dùng đến Google để đăng nhập.
//2️,Google redirect về frontend có đường dân redirectUri kèm theo mã code(1 chuỗi ngẫu nhiên do google tạo có hiệu lực vài phút).
//3,️Frontend nhận thông tin từ google trả về và gửi code lên backend.
//4️,Backend đổi code lấy Access Token từ Google(sử dung exchangeToken để gửi code ngẫu nhiên này lại cho google đổi lấy accessToken của google).
//5, Backend dùng Access Token lấy thông tin user từ Google.(sử dụng getUserInfo giải mã accessToken và lấy thông tin từ google)
//6,Backend tạo JWT Token và gửi về frontend.(nếu có tồn tại user rồi thì gửi accessToken của hệ thông và luôn,mà nếu chưa có thì tạo ra người dùng và gửi về AccessToken)
}