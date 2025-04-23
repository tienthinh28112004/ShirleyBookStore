package ApiWebManga.service.Impl;

import ApiWebManga.Entity.Roles;
import ApiWebManga.Entity.User;
import ApiWebManga.Entity.UserHasRoles;
import ApiWebManga.Exception.NotFoundException;
import ApiWebManga.Exception.TokenExpiredException;
import ApiWebManga.repository.UserRepository;
import ApiWebManga.service.JwtService;
import ApiWebManga.service.RedisService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements JwtService {
    private final UserRepository userRepository;

    private final RedisService redisService;
    @Value("${app.secret}")
    private String appSecret;

    @Value("${app.jwt.token.expires-in}")
    private Long tokenExpiresIn;

    @Value("${app.jwt.refresh-token.expires-in}")
    private Long refreshTokenExpiresIn;

    public String generateAccessToken(User user){//sử dụng thư viện JOSE để tạo token
        JWSHeader header=new JWSHeader(JWSAlgorithm.HS512);
        if (user.getId() == null) {
            throw new IllegalArgumentException("User ID is null");
        }
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())//
                .issuer("TienThinh")//
                .issueTime(new Date())
                .expirationTime(new Date(new Date().getTime() + tokenExpiresIn))//
                .jwtID(UUID.randomUUID().toString()) //theo devteria thì nên dùng và lưu trong csdl
                .claim("Authority",buildAuthority(user))
                .build();

        Payload payload=new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject =new JWSObject(header,payload);

        try {
            jwsObject.sign(new MACSigner(appSecret));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }
    public String generateRefreshToken(User user){//sử dụng thư viện JOSE để tạo token
        JWSHeader header=new JWSHeader(JWSAlgorithm.HS512);
        if (user.getId() == null) {
            throw new IllegalArgumentException("User ID is null");
        }
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())//
                .issuer("TienThinh")//
                .issueTime(new Date())
                .expirationTime(new Date(new Date().getTime() + refreshTokenExpiresIn))//
                .build();

        Payload payload=new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject =new JWSObject(header,payload);

        try {
            jwsObject.sign(new MACSigner(appSecret));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }
    @Override
    public SignedJWT extractUserSignedJWT(String accessToken) throws ParseException, JOSEException {
        try{
            SignedJWT signedJWT=SignedJWT.parse(accessToken);
            if(verifyToken(accessToken)){
                return signedJWT;//userid
            }
        } catch (Exception e) {
            throw new BadCredentialsException("Token invalid");
        }
        return null;
    }

    @Override
    public boolean verifyToken(String token) throws ParseException, JOSEException {
        //cái này chỉ dành cho refreshToken là chue yếu
        Boolean verified = SignedJWT.parse(token).verify(new MACVerifier(appSecret));
        if(!(verified && !isTokenExpired(token))) throw new TokenExpiredException("Xác minh token thất bại!");
        return true;
    }

    @Override
    public boolean isTokenExpired(String token) throws ParseException, JOSEException {
        Date expirationDate = SignedJWT.parse(token).getJWTClaimsSet().getExpirationTime();
        return expirationDate.before(new Date());//còn hạn trả về false,hết thì trả về true
    }

    @Override
    public User getUserFromToken(String token) {
        try {
            String email = extractEmail(token);
            return userRepository.findByEmail(email).orElseThrow(()->new NotFoundException("user not found"));
        } catch (NotFoundException | ParseException | JOSEException e) {
            return null;
        }
    }

    @Override
    public String extractEmail(String token) throws ParseException, JOSEException {
        return extractUserSignedJWT(token).getJWTClaimsSet().getSubject();
    }

    @Override
    public Long extractTokenExpired(String token) {
        try {
            long expirationTime = SignedJWT.parse(token)
                    .getJWTClaimsSet().getExpirationTime().getTime();
            long currentTime = System.currentTimeMillis();
            return Math.max(expirationTime -currentTime,0);
        } catch (ParseException e) {
            log.info("xuất hiện lỗi");
            throw new RuntimeException(e);
        }
    }

    public boolean verificationToken(String token,User user){
        try {//cái verify này dành riêng cho access
            String jwtId=extractUserSignedJWT(token).getJWTClaimsSet().getJWTID();
            if(StringUtils.hasLength(redisService.get(jwtId))){
                throw  new TokenExpiredException("Token của bạn đã hết hạn");
            }
            String email=extractEmail(token);
            if(!Objects.equals(email,user.getEmail())){
                log.error("Email in token not match email system!");
                throw new TokenExpiredException("token invalid");
            }
            return verifyToken(token);
        }catch (Exception e){
            log.error("validate token not successfull because {}",e.getMessage());
            return false;
        }
    }

    private List<String> buildAuthority(User user){
//        StringJoiner roles=new StringJoiner(" ");
        ArrayList<String> roles=new ArrayList<>();
        user.getUserHasRoles().stream().map(UserHasRoles::getRole).map(Roles::getName).forEach(roles::add);
        return roles;//trả ra dạng "[ADMIN,USER] "
    }

//    private String buildPermission(User user){
//        StringJoiner permission = new StringJoiner(" ");
//        if(!CollectionUtils.isEmpty(user.getUserHasRoles())){
//            user.getUserHasRoles().stream().map(UserHasRoles::getRole)
//                    .flatMap(roles -> roles.getRoleHasPermissions().stream().map(rolesHasPermission -> rolesHasPermission.getPermission().getName()))
//                            .distinct()//đảm bảo các quyền của các roles khng trùng nhau khi joiner vào permission
//                            .forEach(permission::add);
//        }
//        return permission.toString();//trả ra dạng "CREATE UPDATE ..."
//    }
}
