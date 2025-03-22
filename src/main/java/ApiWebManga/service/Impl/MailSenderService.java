package ApiWebManga.service.Impl;

import ApiWebManga.Entity.User;
import ApiWebManga.Exception.NotFoundException;
import ApiWebManga.repository.UserRepository;
import ApiWebManga.service.EmailVerificationTokenService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;



import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailSenderService {
    private final JavaMailSender javaMailSender;
    private final MessageSourceService messageSourceService;
    private final UserRepository userRepository;
    private final EmailVerificationTokenService emailVerificationTokenService;
    @Value("${spring.application.name}")
    private String webName;//apiWebManga

    @Value("${spring.mail.username}")
    private String senderAddress;//tienthinh28112004@gmail.com

    public void sendEmailUser(User user) throws MessagingException, MailException {//bắt lối gửi mail
        try{
            String frontEndUrl = "http://localhost:3000";//email này phải c dạng trùng với erify email trong authenticate để nso có thể chạy đến và xác thực
            String url = String.format("%s/auth/email-verification/%s",frontEndUrl,
                    user.getEmailVerificationToken().getToken());

            // Tạo nội dung HTML trực tiếp
            String htmlContent = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head><meta charset=\"UTF-8\"/><title>Email Verification</title></head>" +
                    "<body>" +
                    "<h1>Welcome, " + user.getFullName() + "!</h1>" +
                    "<p>Please click the link below to verify your email:</p>" +
                    "<a href=\"" + url + "\">Verify Email</a>" +
                    "<p>Or return to our website:</p>" +
                    "<a href=\"" + frontEndUrl + "\">Back to " + webName + "</a>" +
                    "</body>" +
                    "</html>";
            MimeMessage message=javaMailSender.createMimeMessage();//tạo đối tượng MIME giúp hỗ trợ các ngôn ng văn bản tệp đính kèm
            MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(message,true);//true ở đây tức là cho phép hỗ trợ nhiều dạng như văn abnr thuần hay html
            mimeMessageHelper.setFrom(senderAddress);//địa chỉ gửi
            mimeMessageHelper.setTo(user.getEmail());//địa chỉ email người nhận
            mimeMessageHelper.setSubject("Verify Email Your:");
            mimeMessageHelper.setText(htmlContent,true);//true là để xác nhận gửi bằng html

            javaMailSender.send(message);//gửi thông tin
        }catch (RuntimeException e){
            log.error("xác minh email thất bại");
        }
    }

}
