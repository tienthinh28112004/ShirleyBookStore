package ApiWebManga.service;

import ApiWebManga.dto.Response.BookDetailResponse;

import java.util.List;

public interface FavoriteService {
    boolean toggleFavorite(Long bookId);
    List<BookDetailResponse> getFavoriteBook();
}
