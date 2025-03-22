package ApiWebManga.dto.Response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
public class CartInformationResponse implements Serializable {
    private Long cartId;
    private Long userId;
    private Long totalElements;
    private Long totalMoney;
    private List<CartItemResponse> items;
}
