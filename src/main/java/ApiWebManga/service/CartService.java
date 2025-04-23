package ApiWebManga.service;

import ApiWebManga.dto.Request.CartAddItemRequest;
import ApiWebManga.dto.Response.CartTotalResponse;

public interface CartService {
    //CartInformationResponse createDefaultCart();
    CartTotalResponse addOrUpdateItemCart(CartAddItemRequest request);
    void deleteItemCart(Long bookId);
    void clearAllItemCart();
    CartTotalResponse informationCart();

}
