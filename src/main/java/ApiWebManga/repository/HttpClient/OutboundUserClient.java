package ApiWebManga.repository.HttpClient;

import ApiWebManga.dto.Response.OutboundUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "outbound-user-client",url = "http://www.googleapis.com")
public interface OutboundUserClient {
    @GetMapping(value = "/oauth2/v1/userinfo")
    OutboundUserResponse getUserInfo(@RequestParam("alt") String alt,//dùng để định dạng(thường là json)
                                     @RequestParam("access_token") String accessToken);//accessToken để xác thực
}
//gửi yêu cầu đến google để lấy thông tin user nếu hợp lệ trả ra thông tin user,nếu không trả ra lỗi 401 Unauthorized
//đường dẫn sẽ có dạng GET http://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=ya29.a0AfH6S...
