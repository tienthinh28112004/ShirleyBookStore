package ApiWebManga.service.Impl;

import ApiWebManga.Entity.EmailVerificationToken;
import ApiWebManga.Entity.User;
import ApiWebManga.Exception.NotFoundException;
import ApiWebManga.repository.EmailVerificationTokenRepository;
import ApiWebManga.service.EmailVerificationTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailVerificationTokenServiceImpl implements EmailVerificationTokenService {
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    @Value("${app.registration.email.token.expires-in}")
    private Long expiresIn;
    @Override
    public EmailVerificationToken create(User user) {
        String newToken = generateToken();
        Date expirationDate = Date.from(Instant.now().plusSeconds(expiresIn));
        EmailVerificationToken oldToken = emailVerificationTokenRepository.findByUserId(user.getId());

        if(oldToken!=null){
            oldToken.setToken(newToken);//nếu như trong csdl đã tồn tại token rồi thì gán lại cho nó token mới
            oldToken.setExpirationDate(expirationDate);//reset lại cả thời gian mới cho nos

            return emailVerificationTokenRepository.save(oldToken);//sau khi xong thì lưu vào csdl
        }else{
            //nếu email chưa lưu token thì sẽ tạo ra 1 cái xác thucj email mới
            return emailVerificationTokenRepository.save(
                    EmailVerificationToken.builder()
                            .user(user)
                            .token(newToken)//tạo ra 1 cái xaác thực email mới
                            .expirationDate(Date.from(Instant.now().plusSeconds(expiresIn)))
                            .build()
            );
        }
    }

    @Override
    public User getUserByToken(String token) {
        //từ cái token ất lấy ra thông tin user
        EmailVerificationToken emailVerificationToken = emailVerificationTokenRepository.findByToken(token)
                .orElseThrow(()->new NotFoundException("EmailVerificationToken not found"));
        if(isEmailVerificationTokenExpired(emailVerificationToken)){
            //do ở phần user chúng ta đã save nó 1 lần rồi,trước acr khi xác minh nên ở đây chúng ta phải kiểm tra lại
            //đề phòng trường hợp chúng ta save và có gửi email nhưng người dùng không verify
            //kiểm tra xem EmailVerificationToken còn hạn hay không
            throw new BadCredentialsException("EmailVerificationToken hết hạn rồi");
        }
        return emailVerificationToken.getUser();
    }

    @Override
    public void deleteByUserId(Long userId) {
        emailVerificationTokenRepository.deleteByUserId(userId);
    }
    public boolean isEmailVerificationTokenExpired(EmailVerificationToken token){
        return token.getExpirationDate().before(new Date());//(before là trước)
        //kiểm tra xem email xác thực này còn hạn không,nếu còn hạn trả về false,hết hạn trả về true
    }
    public String generateToken(){
        Random random =new Random();
        long token =10000000 + random.nextInt(90000000);// Mã OTP 6 chữ số
        return String.valueOf(token);
    }
    //nguyên lý của xác thực mail là người dùng tạo tài khoản
    //chúng ta gửi Email verify token vầ có thời hạn 5 phút trong mail của người dùng,đồng thời nó cũng sẽ luuw 1 bản trong csdl
    //khi người dùng nhấn vào email thì n sẽ tự động link đến chỗ xác minh
    //ở chỗ xác minh nó sẽ xác định thời hạn xác minh và xóa cái emial verify token ấy khỏi csdl,đồng thời lưu user
}
