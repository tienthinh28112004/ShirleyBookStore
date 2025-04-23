package ApiWebManga.dto.Response;

import ApiWebManga.Entity.Order;
import ApiWebManga.Enums.OrderStatus;
import ApiWebManga.Enums.PaymentExpression;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class OrderResponse {
    private Long orderId;

    private String fullName;

    private String phoneNumber;

    private String address;

    private String note;

    private LocalDateTime dateTime;

    private String email;

    private OrderStatus orderStatus;

    private PaymentExpression paymentExpression;

    private Long totalMoney;

    private List<OrderDetailResponse> detailResponse;

    public static OrderResponse convert(Order order){
        return OrderResponse.builder()
                .orderId(order.getId())
                .address(order.getAddress())
                .fullName(order.getFullName())
                .note(order.getNote())
                .phoneNumber(order.getPhoneNumber())
                .dateTime(order.getOrderDate())
                .orderStatus(order.getOrderStatus())
                .email(order.getUser().getEmail())
                .paymentExpression(order.getPaymentExpression())
                .totalMoney(order.getTotalMoney())
                .detailResponse(order.getOrderDetails().stream().map(OrderDetailResponse::convert)
                        .collect(Collectors.toList()))
                .build();
    }
}
