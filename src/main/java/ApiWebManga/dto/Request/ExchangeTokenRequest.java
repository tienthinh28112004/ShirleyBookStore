package ApiWebManga.dto.Request;

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
//đơn giản là chuẩn hóa nó về dang camelcase tương tự JsonProperty nhưng không phải làm cho từng trường 1 mà làm cho toàn bộ(vd clientId=>client_id)(thấy vô dụng vaz(-.-))
public class ExchangeTokenRequest {
    private String code;//code do google cung cấp khi đằn nhập thành công,gửi đến google Token endpoint để đổi lấy accessToken và refreshToken
    private String clientId;//id của ứng dụng
    private String clientSecret;//mật khẩu id của ứng dụng
    private String redirectUri;//url mà google gửi mã code sau khi đăng nhập thành công(đảm bảo gửi về đúng nơi được phép)
    private String grantType;//loại yêu cầu trao đổi token(thường là authorization_code
}
