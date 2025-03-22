package ApiWebManga.Controller;

import ApiWebManga.Entity.Category;
import ApiWebManga.dto.Request.ApiResponse;
import ApiWebManga.dto.Response.BookDetailResponse;
import ApiWebManga.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/favorite")
public class FavoriteController {
//    boolean toggleFavorite(Long bookId);
//    List<Long> getFavoriteBookIds();
    private final FavoriteService favoriteService;
    @PostAuthorize("USER")
    @PostMapping(value = "/toggleFavorite/{bookId}")//thay đổi trạng thái yêu thích truyện của người dùng
    @Operation(summary = "toggle favorite")
    public ApiResponse<Boolean> toggleFavorite (@PathVariable Long bookId) {
        return ApiResponse.<Boolean>builder()
                .message("change status successfully")
                .result(favoriteService.toggleFavorite(bookId))
                .build();
    }

    @PostAuthorize("USER")
    @GetMapping(value = "/getFavoriteBook")//lấy ra danh sách người đang đăng nhập thích
    @Operation(summary = "getFavoriteBook")
    public ApiResponse<List<BookDetailResponse>>  getFavoriteBook() {
        return ApiResponse.<List<BookDetailResponse>>builder()
                .result(favoriteService.getFavoriteBook())
                .build();
    }
}
