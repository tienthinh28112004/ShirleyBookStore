package ApiWebManga.Controller;

import ApiWebManga.dto.Request.ApiResponse;
import ApiWebManga.dto.Request.CartAddItemRequest;
import ApiWebManga.dto.Response.CartTotalResponse;
import ApiWebManga.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/addOrUpdateItem")
    ApiResponse<CartTotalResponse> addItemCart(@RequestBody @Valid CartAddItemRequest request) {
        return ApiResponse.<CartTotalResponse>builder()
                .message("update cart success")
                .result(cartService.addOrUpdateItemCart(request))
                .build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/detailCart")
    ApiResponse<CartTotalResponse> DetailCart() {
        return ApiResponse.<CartTotalResponse>builder()
                .message("update cart success")
                .result(cartService.informationCart())
                .build();
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/delete/{bookId}")
    ApiResponse<Void> deleteItemCart(@PathVariable Long bookId) {
        cartService.deleteItemCart(bookId);
        return ApiResponse.<Void>builder()
                .message("Delete cart success")
                .build();
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/deleteAll")
    ApiResponse<Void> deleteAllItemCart() {
        cartService.clearAllItemCart();
        return ApiResponse.<Void>builder()
                .message("Delete all item cart success")
                .build();
    }

}