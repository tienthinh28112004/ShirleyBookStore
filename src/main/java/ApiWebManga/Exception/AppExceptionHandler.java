package ApiWebManga.Exception;


import ApiWebManga.Utils.LocalizationUtils;
import ApiWebManga.service.Impl.MessageSourceService;
import ApiWebManga.dto.Response.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@ControllerAdvice
@Slf4j
public class AppExceptionHandler {
    private final LocalizationUtils localizationUtils;
    @ExceptionHandler({
            BindException.class,
            MethodArgumentNotValidException.class//băt các phương thức liên quan đến dữ liệu
    })
    public ResponseEntity<ErrorResponse> handleBindException(BindException e){//xảy ra khi dữ lệu vào không khớp
        Map<String,String> errors=new HashMap<>();

        e.getBindingResult().getAllErrors().forEach(error ->{
            String fieldName=((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName,message);
        });
        return build(HttpStatus.UNPROCESSABLE_ENTITY, localizationUtils.getLocalizedMessage("validation_error"),errors);
    }

    @ExceptionHandler({
            BadRequestException.class,//lỗi khi tham số không hợp lệ hoặc cấu trúc request sai
            MultipartException.class,//lỗi xảy ra khi xử lý upload file
            MissingServletRequestPartException.class,//xảy ra khi upload file bị thiếu
            MethodArgumentTypeMismatchException.class,//dữ liệu tham số không khớp kiểu mong muốn
            IllegalArgumentException.class,//xảy ra khi đối số không hợp lệ truyền vào 1 phương thức
            InvalidDataAccessApiUsageException.class,//sai Api khi truy cập dữ liệu
            ConstraintViolationException.class,//lỗi xảy ra khi các anotation @Size @Min bị vi phạm
    })
    public ResponseEntity<ErrorResponse> handleBadRequestException(final Exception e) {
        log.error(e.toString(), e.getMessage());
        return build(HttpStatus.BAD_REQUEST, e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
    }

    @ExceptionHandler({
            TokenExpiredException.class,
            RefreshTokenExpiredException.class,
    })
    public ResponseEntity<ErrorResponse> handleTokenExpiredRequestException(
             TokenExpiredException e) {
        log.error(e.toString(), e.getMessage());
        return build(HttpStatus.UNAUTHORIZED, e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(final NotFoundException e) {
        log.error(e.toString(), e.getMessage());
        return build(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)//lỗi khi truy cập vào hành động mà không được phép
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(final Exception e) {
        log.error(e.toString(), e.getMessage());
        return build(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler({
            BadCredentialsException.class,//thông tin xác thực không hợp lệ
            AuthenticationCredentialsNotFoundException.class//thông tin xác thực không được cung cấp
    })
    public  ResponseEntity<ErrorResponse> handleBadCredentialsException(final Exception e) {
        log.error(e.toString(), e.getMessage());
        return build(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(Exception.class)//tất cả các lỗi mà bên trên không bắt được sẽ chạy vào đây
    public ResponseEntity<ErrorResponse> handleAllExceptions(final Exception e) {
        log.error("Exception: {}", ExceptionUtils.getStackTrace(e));
        return build(HttpStatus.INTERNAL_SERVER_ERROR, localizationUtils.getLocalizedMessage("server_error"));
    }
    private ResponseEntity<ErrorResponse> build(HttpStatus httpStatus,
                                                String message,
                                                Map<String, String> errors) {
        return ResponseEntity.status(httpStatus).body(ErrorResponse.builder()
                .items(errors)
                .message(message)
                .build());
    }
    private ResponseEntity<ErrorResponse> build(final HttpStatus httpStatus, final String message) {
        return build(httpStatus, message, new HashMap<>());
    }
}
