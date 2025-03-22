package ApiWebManga.Controller;

import ApiWebManga.dto.Request.ApiResponse;
import ApiWebManga.dto.Request.CartAddItemRequest;
import ApiWebManga.dto.Response.CartInformationResponse;
import ApiWebManga.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

//    @PostAuthorize("USER")//vào phát là đã có giỏ hàng rồi
//    @PostMapping("/carts")
//    ApiResponse<CartInformationResponse> creationCart() {
//        var result = cartService.createDefaultCart();
//        return ApiResponse.<CartInformationResponse>builder()
//                .message("Created success")
//                .result(result)
//                .build();
//    }

    @PostAuthorize("USER")
    @DeleteMapping("/carts/addItem")
    ApiResponse<CartInformationResponse> addItemCart(@RequestBody @Valid CartAddItemRequest request) {
        return ApiResponse.<CartInformationResponse>builder()
                .message("Delete cart success")
                .result(cartService.addItemCart(request))
                .build();
    }
    @PostAuthorize("USER")
    @DeleteMapping("/carts/{bookId}")
    ApiResponse<Void> deleteItemCart(@PathVariable Long bookId) {
        return ApiResponse.<Void>builder()
                .message("Delete cart success")
                .result(cartService.deleteItemCart(bookId))
                .build();
    }

}