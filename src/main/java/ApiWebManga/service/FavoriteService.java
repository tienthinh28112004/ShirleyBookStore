package ApiWebManga.service;

import ApiWebManga.dto.Request.FavoriteRequest;
import ApiWebManga.dto.Response.FavoriteResponse;
import ApiWebManga.dto.Response.PageResponse;

import java.util.List;

public interface FavoriteService {
    void createFavorite(FavoriteRequest request);
    PageResponse<List<FavoriteResponse>> findAllByUserCurrent(int page, int size);
    void deleteFavorite(Long favoriteId);
    FavoriteResponse findFavoriteById(Long notificationId);
}
