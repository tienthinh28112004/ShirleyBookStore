package ApiWebManga.dto.Response;

import ApiWebManga.Entity.Book;
import ApiWebManga.Entity.Chapter;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@Slf4j
public class BookDetailResponse implements Serializable {
    private Long id;
    private String authorName;
    private String title;
    private String isbn;
    private String description;
    private Long price;
    private String language;
    private String thumbnail;
    private String bookPath;
    private List<String> category;
    private List<Chapter> chapter;

    public static BookDetailResponse convert(Book book){
        List<String> listCategory = new ArrayList<>();
        book.getCategory().stream().map(bookHasCategory ->bookHasCategory.getCategory().getName()).forEach(listCategory::add);
        return BookDetailResponse.builder()
                        .id(book.getId())
                        .authorName(book.getAuthor().getFullName())
                        .title(book.getTitle())
                        .isbn(book.getIsbn())
                        .description(book.getDescription())
                        .language(book.getLanguage())
                        .price(book.getPrice())
                        .chapter(book.getChapters())
                        .thumbnail(book.getThumbnail())
                        .bookPath(book.getBookPath())
                        .category(listCategory)
                .build();
    }
}
