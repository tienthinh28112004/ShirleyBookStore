package ApiWebManga.service;

import ApiWebManga.dto.Request.ApiResponse;
import ApiWebManga.dto.Request.PaymentRequest;
import ApiWebManga.dto.Response.TransactionStatusResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;

public interface PaymentService {
    String paymentCreate(HttpServletRequest req, PaymentRequest request) throws UnsupportedEncodingException;
    TransactionStatusResponse transactionStatus(String amount, String bankCode, String order, String responseCode);
}
