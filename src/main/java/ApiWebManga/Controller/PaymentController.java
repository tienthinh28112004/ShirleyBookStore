package ApiWebManga.Controller;

import ApiWebManga.config.VnpayConfig;
import ApiWebManga.dto.Request.ApiResponse;
import ApiWebManga.dto.Request.PaymentRequest;
import ApiWebManga.dto.Response.TransactionStatusResponse;
import ApiWebManga.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    //chạy và vào http://localhost:8080/ApiWebManga/api/payment/create_payment để lấy thông tin
    @PostMapping("/create_payment")
    public ApiResponse<?> createPayment(HttpServletRequest req,@RequestBody @Valid PaymentRequest request) throws UnsupportedEncodingException {
        return ApiResponse.builder()
                .message("Thanh toán thành công")
                .result(paymentService.paymentCreate(req,request))
                .build();
    }

    @GetMapping("/payment_info")//dữ liệu cần ấy xuống trên thanh url
    public ApiResponse<?> transaction(
            @RequestParam(value = "vnp_Amount") String amount,
            @RequestParam(value = "vnp_BankCode") String bankCode,
            @RequestParam(value = "vnp_OrderInfo") String order,
            @RequestParam(value = "vnp_ResponseCode") String responseCode
    ){
        return ApiResponse.builder()
                .message("thông tin chi tiết về đơn thanh toán")
                .result(paymentService.transactionStatus(amount,bankCode,order,responseCode))
                .build();
    }
}
