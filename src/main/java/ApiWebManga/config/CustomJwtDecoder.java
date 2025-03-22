package ApiWebManga.config;

import ApiWebManga.Entity.User;
import ApiWebManga.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${app.secret}")
    private String appSecret;

    private final JwtService jwtService;

    private NimbusJwtDecoder nimbusJwtDecoder=null;

    @Override
    public Jwt decode(String token) throws JwtException {
        User user=jwtService.getUserFromToken(token);
        if(!jwtService.verificationToken(token,user)) throw new JwtException("Token invalid");

        if(Objects.isNull(nimbusJwtDecoder)){
            SecretKeySpec secretKeySpec=new SecretKeySpec(appSecret.getBytes(),"HS512");//HS512 ở đây chỉ để bổ sung thông tin thôi
            nimbusJwtDecoder = NimbusJwtDecoder
                    .withSecretKey(secretKeySpec)//tryền secretKeySpec vào
                    .macAlgorithm(MacAlgorithm.HS512)//thuật toán mã hóa
                    .build();
        }
        return nimbusJwtDecoder.decode(token);//giải mã cái token
    }
}
