package ApiWebManga.Controller;

import ApiWebManga.dto.Request.ApiResponse;
import ApiWebManga.dto.Request.PaymentRequest;
import ApiWebManga.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    //chạy và vào http://localhost:8080/ApiWebManga/api/payment/create_payment để lấy thông tin
    @PostMapping("/create_payment")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<?> createPayment(HttpServletRequest req,
                                        @RequestBody @Valid PaymentRequest request) throws UnsupportedEncodingException {
        return ApiResponse.builder()
                .message("Thanh toán thành công")
                .result(paymentService.createVnPayPayment(request,req))
                .build();
    }

    @GetMapping("/vn-pay-callback")//dữ liệu cần ấy xuống trên thanh url
    public void handleVnPayCallback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String redirectUrl;
        String transactionStatus = request.getParameter("vnp_ResponseCode");
        if ("00".equals(transactionStatus)) {
            redirectUrl = "http://localhost:3000/payment-success";
        } else if ("24".equals(transactionStatus)) {
            redirectUrl = "http://localhost:3000/payment-cancel";
        } else {
            redirectUrl = "http://localhost:3000/payment-failed";
        }
        response.sendRedirect(redirectUrl);
    }
//fontend gửi request thanh toán lên backend,backend xử lý tính toán trả về đường dẫn cho fontend
    //fontend mở đường dẫn reddirect đến trang thanh toán,sau khi thanh toán vnpay thanh công sẽ có mã phải hồi về backend
    //backedn nhận mã ấy và response giao diện phù hợp
}
