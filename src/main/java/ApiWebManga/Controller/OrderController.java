package ApiWebManga.Controller;

import ApiWebManga.Enums.OrderStatus;
import ApiWebManga.Enums.PaymentExpression;
import ApiWebManga.dto.Request.ApiResponse;
import ApiWebManga.dto.Request.OrderRequest;
import ApiWebManga.dto.Response.OrderResponse;
import ApiWebManga.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/order")
public class OrderController {
    public final OrderService orderService;
    @PostAuthorize("USER")
    @PostMapping("/createOrder")
    public ApiResponse<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        return ApiResponse.<OrderResponse>builder()
                .message("create order successfully")
                .result(orderService.createOrder(orderRequest))
                .build();
    }

    @PostAuthorize("#email == authentication.token.claims['sub'] or hasRole('ADMIN')")
    @GetMapping("/getOrder/{id}")
    public ApiResponse<OrderResponse> getOrderById(@PathVariable Long id) {
        return ApiResponse.<OrderResponse>builder()
                .message("information order successfully")
                .result(orderService.informationCart(id))
                .build();
    }

    @PostAuthorize("#email == authentication.token.claims['sub'] or hasRole('ADMIN')")
    @GetMapping("/getHistoryOrder")
    public ApiResponse<List<OrderResponse>> getHistoryOrder() {
        return ApiResponse.<List<OrderResponse>>builder()
                .message("information historyOrder")
                .result(orderService.findOrderByUser())
                .build();
    }
    @PostAuthorize("ADMIN")
    @GetMapping("/getOrderRecent")
    public ApiResponse<List<OrderResponse>> getOrderRecent(
            @RequestParam(defaultValue = "1",required = false) int page,
            @RequestParam(defaultValue = "10",required = false) int size
    ) {
        return ApiResponse.<List<OrderResponse>>builder()
                .message("OrderRecent")
                .result(orderService.orderRecent(page,size))
                .build();
    }

    @PostAuthorize("ADMIN")
    @PutMapping("/{orderId}/orderStatus")
    public ApiResponse<?> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String newStatus
    ) {
        orderService.updateOrderStatus(orderId, OrderStatus.valueOf(newStatus.toUpperCase()));
        return ApiResponse.<Void>builder()
                .message("change orderStatus successfully")
                .build();
    }

    @PostAuthorize("ADMIN")
    @PutMapping("/{orderId}/paymentStatus")
    public ApiResponse<?> updateStatusPayment(
            @PathVariable Long orderId,
            @RequestParam String newStatus
    ) {
        orderService.updatePaymentExpression(orderId, PaymentExpression.valueOf(newStatus));
        return ApiResponse.<Void>builder()
                .message("update Status Payment")
                .build();
    }
}
