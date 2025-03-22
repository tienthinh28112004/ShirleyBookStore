package ApiWebManga.dto.Response;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponse implements Serializable {
    private Long bookId;
    private String title;
    private Long priceBook;
    private String thumbnail;
    private Long quantity;
    private Long totalPrice;
}
