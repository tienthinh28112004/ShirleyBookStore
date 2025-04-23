package ApiWebManga.Controller;

import ApiWebManga.Enums.OrderStatus;
import ApiWebManga.Enums.PaymentExpression;
import ApiWebManga.dto.Request.ApiResponse;
import ApiWebManga.dto.Request.OrderRequest;
import ApiWebManga.dto.Response.OrderResponse;
import ApiWebManga.dto.Response.PageResponse;
import ApiWebManga.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/order")
public class OrderController {
    public final OrderService orderService;

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/createOrder")
    public ApiResponse<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        return ApiResponse.<OrderResponse>builder()
                .message("create order successfully")
                .result(orderService.createOrder(orderRequest))
                .build();
    }

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @GetMapping("/getOrder/{id}")
    public ApiResponse<OrderResponse> getOrderById(@PathVariable Long id) {
        return ApiResponse.<OrderResponse>builder()
                .message("information order successfully")
                .result(orderService.informationOrder(id))
                .build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/getHistoryOrder")
    public ApiResponse<List<OrderResponse>> getHistoryOrder() {
        return ApiResponse.<List<OrderResponse>>builder()
                .message("information historyOrder")
                .result(orderService.findOrderByUser())
                .build();
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/getOrderRecent")
    public ApiResponse<PageResponse<List<OrderResponse>>> getOrderRecent(
            @RequestParam(defaultValue = "1",required = false) int page,
            @RequestParam(defaultValue = "10",required = false) int size
    ) {
        return ApiResponse.<PageResponse<List<OrderResponse>>>builder()
                .message("OrderRecent")
                .result(orderService.orderRecent(page,size))
                .build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{orderId}/orderStatus")
    public ApiResponse<?> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String newStatus
    ) {
        orderService.updateOrderStatus(orderId, OrderStatus.valueOf(newStatus.toUpperCase()));
        log.info("orderId {}",orderId);
        return ApiResponse.<Void>builder()
                .message("change orderStatus successfully")
                .build();
    }

    @PreAuthorize("hasAuthority('USER')")
    @PatchMapping("/{orderId}/paymentStatus")
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
