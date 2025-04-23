package ApiWebManga.config;

import ApiWebManga.Exception.AppExceptionHandler;
import ApiWebManga.Utils.LocalizationUtils;
import ApiWebManga.dto.Response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final LocalizationUtils localizationUtils;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        //lấy ra danh sách các lỗi tạo ra lỗi 401
        log.info("đến đây chưa?");
        ResponseEntity<ErrorResponse> responseEntity = new AppExceptionHandler(localizationUtils)//đăng nhập không hợp lệ
                .handleBadCredentialsException(new BadCredentialsException(localizationUtils.getLocalizedMessage("bad_credentials")));

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//trả về status 401 unauthenticate
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);//chuyển dữ liệu trả về sang dạng json

        //do write trả về kiểu dữ liệu dạng String nên ở đây chúng ta phải dùng ObjectMapper để convert apiResponse nó sang dạng json
        ObjectMapper objectMapper = new ObjectMapper();//dùng objectMapper ở đây để convert từ object sang dạng json
        response.getWriter().write(objectMapper.writeValueAsString(responseEntity));
        log.info("Đăng nhập không thành công");
        response.flushBuffer();//flushBuffer giúp gửi cái request này về client
    }
}
