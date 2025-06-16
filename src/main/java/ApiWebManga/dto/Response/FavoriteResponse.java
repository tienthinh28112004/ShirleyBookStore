package ApiWebManga.dto.Response;

import ApiWebManga.Entity.Favorite;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteResponse {
    private Long favoriteId;

    private String name;

    private String author;

    private Long priceBook;

    private String title;

    private String thumbnail;

    private Long bookId;

    public static FavoriteResponse convert(Favorite favorite){
        return FavoriteResponse.builder()
                .name(favorite.getUser().getFullName())
                .favoriteId(favorite.getId())
                .bookId(favorite.getBook().getId())
                .priceBook(favorite.getBook().getPrice())
                .title(favorite.getBook().getTitle())
                .thumbnail(favorite.getBook().getThumbnail())
                .author(favorite.getBook().getAuthor()!=null?favorite.getBook().getAuthor().getFullName():favorite.getBook().getAuthorName())
                .build();
    }
}
