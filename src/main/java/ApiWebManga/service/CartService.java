package ApiWebManga.service;

import ApiWebManga.dto.Request.CartAddItemRequest;
import ApiWebManga.dto.Response.CartInformationResponse;

public interface CartService {
    //CartInformationResponse createDefaultCart();
    CartInformationResponse addItemCart(CartAddItemRequest request);
    Void deleteItemCart(Long bookId);
    CartInformationResponse informationCart();

}
