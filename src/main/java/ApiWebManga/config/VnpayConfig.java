package ApiWebManga.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
//đây là file dùng để thanh toán vnpay lấy được trên vnpay dev test(tuy nhiên chỉ chạy được trong môi trường test vì nếu muốn chạy thật thì pahir đăng kí doanh nghiêp)
public class VnpayConfig {
    @Getter
    @Value("${payment.vnPay.url}")
    private String vnp_PayUrl;

    @Value("${payment.vnPay.returnUrl}")
    private String vnp_ReturnUrl;//sau khi vnpay xử lý thành công thì nó sẽ vào backend v xử lý tại đây

    @Value("${payment.vnPay.tmnCode}")
    private String vnp_TmnCode;

    @Getter
    @Value("${payment.vnPay.secretKey}")
    private String secretKey;

    @Value("${payment.vnPay.version}")
    private String vnp_Version;

    @Value("${payment.vnPay.command}")
    private String vnp_Command;

    @Value("${payment.vnPay.orderType}")
    private String orderType;

    public Map<String,String> getVNPayConfig(){
        Map<String,String> vnpParamsMap = new HashMap<>();
        vnpParamsMap.put("vnp_Version",this.vnp_Version);
        vnpParamsMap.put("vnp_Command",this.vnp_Command);
        vnpParamsMap.put("vnp_TmnCode",this.vnp_TmnCode);
        vnpParamsMap.put("vnp_CurrCode","VND");
        vnpParamsMap.put("vnp_OrderType",this.orderType);
        vnpParamsMap.put("vnp_Locale","vn");
        vnpParamsMap.put("vnp_ReturnUrl",this.vnp_ReturnUrl);
        return vnpParamsMap;
    }
}
