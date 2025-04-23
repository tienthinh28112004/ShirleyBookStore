package ApiWebManga.Controller;

import ApiWebManga.dto.Request.ApiResponse;
import ApiWebManga.dto.Request.FavoriteRequest;
import ApiWebManga.dto.Response.FavoriteResponse;
import ApiWebManga.dto.Response.PageResponse;
import ApiWebManga.service.FavoriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/favorite")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PreAuthorize("isAuthenticated() or hasAuthority('USER')")
    @GetMapping(value = "/favoriteUserCurrent")
    public ApiResponse<PageResponse<List<FavoriteResponse>>> favoriteUserCurrent (
            @RequestParam(required = false,defaultValue = "1") int page,
            @RequestParam(required = false,defaultValue = "10") int size
    ) {
        return ApiResponse.<PageResponse<List<FavoriteResponse>>>builder()
                .message("change status successfully")
                .result(favoriteService.findAllByUserCurrent(page,size))
                .build();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/createFavorite")
    public ApiResponse<Void> createFavorite (@RequestBody @Valid FavoriteRequest request) {
        favoriteService.createFavorite(request);
        return ApiResponse.<Void>builder()
                .message("create favorite successfully")
                .build();
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping(value = "/deleteFavorite/{favoriteId}")
    public ApiResponse<Void> deleteFavorite (@PathVariable Long favoriteId) {
        favoriteService.deleteFavorite(favoriteId);
        return ApiResponse.<Void>builder()
                .message("delete favorite successfully")
                .build();
    }

    @PreAuthorize("isAuthenticated() or hasAuthority('ADMIN')")
    @GetMapping(value = "/getFavorite/{favoriteId}")
    public ApiResponse<FavoriteResponse> getFavorite (@PathVariable Long favoriteId) {
        return ApiResponse.<FavoriteResponse>builder()
                .message("get favorite successfully")
                .result(favoriteService.findFavoriteById(favoriteId))
                .build();
    }

}
