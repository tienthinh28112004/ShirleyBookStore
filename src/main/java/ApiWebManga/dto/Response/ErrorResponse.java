package ApiWebManga.dto.Response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class ErrorResponse {
    private String message;
    private Map<String,String> items;
}
//NotFoundException
//BindException  BadRequestException
//AccessDeniedException
//   BadCredentialsException
//CipherException
//AuthenticationCredentialsNotFoundException
//RefreshTokenExpiredException()
//