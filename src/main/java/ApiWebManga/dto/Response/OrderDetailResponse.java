package ApiWebManga.dto.Response;

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
}
