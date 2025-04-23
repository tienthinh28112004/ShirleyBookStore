package ApiWebManga.service;

import ApiWebManga.dto.Request.PaymentRequest;
import ApiWebManga.dto.Response.PaymentResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface PaymentService {
    PaymentResponse createVnPayPayment(PaymentRequest request, HttpServletRequest req);
}
