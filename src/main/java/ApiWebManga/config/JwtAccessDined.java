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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAccessDined implements AccessDeniedHandler {
    private  final LocalizationUtils localizationUtils;
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        //lấy ra danh sách các lỗi tạo ra lỗi 403
        ResponseEntity<ErrorResponse> responseEntity = new AppExceptionHandler(localizationUtils)//không có quyền đăng nhập
                .handleBadCredentialsException(new BadCredentialsException(localizationUtils.getLocalizedMessage("access_denied")));

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);//trả về status 401 unauthenticate
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);//chuyển dữ liệu trả về sang dạng json

        //do write trả về kiểu dữ liệu dạng String nên ở đây chúng ta phải dùng ObjectMapper để convert apiResponse nó sang dạng json
        ObjectMapper objectMapper = new ObjectMapper();//dùng objectMapper ở đây để convert từ object sang dạng json
        response.getWriter().write(objectMapper.writeValueAsString(responseEntity));
        log.info("bạn không có quyền");
        response.flushBuffer();//flushBuffer giúp gửi cái request này về client
    }
}
