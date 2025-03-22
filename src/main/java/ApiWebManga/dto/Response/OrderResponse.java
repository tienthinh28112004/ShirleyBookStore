package ApiWebManga.dto.Response;

import ApiWebManga.Entity.Order;
import ApiWebManga.Enums.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private String fullName;

    private String phoneNumber;

    private String address;

    private String note;

    private LocalDateTime dateTime;

    private String userName;

    private OrderStatus orderStatus;

    private List<OrderDetailResponse> detailResponse;

    public static OrderResponse convert(Order order){
        return OrderResponse.builder()
                .address(order.getAddress())
                .fullName(order.getFullName())
                .note(order.getNote())
                .phoneNumber(order.getPhoneNumber())
                .dateTime(order.getOrderDate())
                .orderStatus(order.getOrderStatus())
                .userName(order.getUser().getFullName())
                .detailResponse(order.getOrderDetails().stream().map(
                        orderDetail -> OrderDetailResponse.builder()
                                .title(orderDetail.getBook().getTitle())
                                .bookId(orderDetail.getBook().getId())
                                .priceBook(orderDetail.getPrice())
                                .quantity(orderDetail.getQuantity())
                                .thumbnail(orderDetail.getBook().getThumbnail())
                                .totalPrice(orderDetail.getPrice())
                                .build()
                ).collect(Collectors.toList()))
                .build();
    }
}
