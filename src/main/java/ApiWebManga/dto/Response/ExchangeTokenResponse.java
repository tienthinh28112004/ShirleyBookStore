package ApiWebManga.dto.Response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ExchangeTokenResponse {//đại diện chp phản hồi từ google
    private String accessToken;//dùng để xác thực và ủy quyền gọi api của google
    private Long expiresToken;//thời gian hết hạn của accessToken
    private String refreshToken;//mã làm mới do google
    private String scope;//danh sách quyền có thể truy nhập(có thể là "https://www.googleapis.com/auth/userinfo.profile")
    private String tokenType;//xác định loàij Token(hầu hết sẽ là Bearer)
}
