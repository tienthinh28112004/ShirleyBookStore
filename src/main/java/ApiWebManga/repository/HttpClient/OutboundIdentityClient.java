package ApiWebManga.repository.HttpClient;

import ApiWebManga.dto.Request.ExchangeTokenRequest;
import ApiWebManga.dto.Response.ExchangeTokenResponse;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
//nôm na là FeignClient cũng tương tự như gửi request nhận dữ liệu thông thường nhưng sẽ đỡ phải viết nhiều hn và hỗ trợ nhiều tính năng
@FeignClient(name="outbound-identity",url = "https://oauth2.googleapis.com")
//outbound-identity là tên của feignClient này
//https://oauth2.googleapis.com url gốc  API Google OAuth2 mà client này sẽ gửi yêu cầu
public interface OutboundIdentityClient {//chỉ cần gọi đúng phương thức google sẽ tự động trả dữ liệu về cho at
    @PostMapping(value = "/token",produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ExchangeTokenResponse exchangeToken(@QueryMap ExchangeTokenRequest request);
    //PostMapping(value="/token") là định nghĩa 1 HTTP Post request đến /token
    //produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE sẽ yêu cầu phản hồi với format application/x-www-form-urlencoded.
    //@QueryMap là phương thức hỗ trợ FeignClient giúp map các dữ liệu từ key-value thành dạng tham số query string
}
//=>dữ liệu gửi đi sẽ có header application/x-www-form-urlencoded và
// dạng POST https://oauth2.googleapis.com/token?client_id=your-client-id
                    //&client_secret=your-client-secret
                    //&grant_type=authorization_code
                    //&code=abc123
                    //&redirect_uri=http://localhost:3000/oauth2/callback/google
//sau khi gửi đi thành công,google sẽ redirect kèm theo mã code về đường link http://localhost:3000/oauth2/callback/google?code=abc123
