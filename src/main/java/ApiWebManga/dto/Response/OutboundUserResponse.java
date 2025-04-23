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
public class OutboundUserResponse {
    String id;//id duy nhất của người dùng trên google
    String email;//địa chỉ email của người dùng
    boolean verified;//true nếu email được google xác minh(có thể tin tưởng)
    String name;//họ tên đầy đủ của người dùng
    String givenName;//tên riêng người dùng(có thể bỏ)
    String familyName;//họ của người dùng(có thể bỏ)
    String picture;//ảnh đại diện của người dùng
    String locale;//ngôn ngữ ưu tiên của người dùng(en,vi,..)
    //còn có thể trả về phone_number,birthday.profile của người đăng nhập abwnfg google
}
