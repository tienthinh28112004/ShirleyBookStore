package ApiWebManga.service.Impl;

import ApiWebManga.Utils.VNPayUtil;
import ApiWebManga.dto.Request.PaymentRequest;
import ApiWebManga.dto.Response.PaymentResponse;
import ApiWebManga.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private  final VNPayUtil vnPayUtil;
    @Override
    public PaymentResponse createVnPayPayment(PaymentRequest request, HttpServletRequest req) {
        String paymentUrl=vnPayUtil.getPaymentURL(request.getAmount(), req);
        return PaymentResponse.builder()
                .code("ok")
                .message("success")
                .paymentUrl(paymentUrl)
                .build();
    }
}
