package ApiWebManga.dto.Response;

import ApiWebManga.Entity.OrderDetail;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailResponse {
    private Long bookId;
    private String title;
    private Long priceBook;
    private String thumbnail;
    private Long quantity;
    private Long totalPrice;

    public static OrderDetailResponse convert(OrderDetail orderDetail){
        return OrderDetailResponse.builder()
                .bookId(orderDetail.getBook().getId())
                .title(orderDetail.getBook().getTitle())
                .priceBook(orderDetail.getBook().getPrice())
                .thumbnail(orderDetail.getBook().getThumbnail())
                .quantity(orderDetail.getQuantity())
                .totalPrice(orderDetail.getTotalMoneyBook())
                .build();
    }
}
