package ApiWebManga.dto.Response;

import ApiWebManga.Entity.CartDetail;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponse implements Serializable {

    private String authorName;
    private Long bookId;
    private String title;
    private Long priceBook;
    private String thumbnail;
    private Long quantity;
    private Long totalPrice;

    public static CartItemResponse convert(CartDetail cartDetail){
        return CartItemResponse.builder()
                .bookId(cartDetail.getBook().getId())
                .authorName(cartDetail.getBook().getAuthor()!=null?cartDetail.getBook().getAuthor().getFullName():cartDetail.getBook().getAuthorName())
                .title(cartDetail.getBook().getTitle())
                .priceBook(cartDetail.getBook().getPrice())
                .thumbnail(cartDetail.getBook().getThumbnail())
                .quantity(cartDetail.getQuantity())
                .totalPrice(cartDetail.getTotalMoney())
                .build();
    }
}
