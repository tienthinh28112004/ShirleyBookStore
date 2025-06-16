package ApiWebManga.service;

import ApiWebManga.Enums.OrderStatus;
import ApiWebManga.Enums.PaymentExpression;
import ApiWebManga.dto.Request.OrderRequest;
import ApiWebManga.dto.Response.OrderResponse;
import ApiWebManga.dto.Response.PageResponse;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest orderRequest);
    List<OrderResponse> findOrderByUser();
    OrderResponse informationOrder(Long orderId);

    void updateOrderStatus(Long orderId, OrderStatus newStatus);
    void updatePaymentExpression(Long orderId, PaymentExpression newPayment);
    PageResponse<List<OrderResponse>> orderRecent(int page, int size);

}
