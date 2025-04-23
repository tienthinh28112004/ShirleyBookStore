package ApiWebManga.dto.Response;

import ApiWebManga.Entity.Book;
import ApiWebManga.Entity.Chapter;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.StringJoiner;

@Getter
@Setter
@Builder
@Slf4j
public class BookDetailResponse implements Serializable {
    private Long id;
    private String authorName;
    private Long authorId;
    private String title;
    private String isbn;
    private String description;
    private Long price;
    private String thumbnail;
    private String bookPath;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Chapter> chapter;

    public static BookDetailResponse convert(Book book){
        StringJoiner joiner= new StringJoiner(",");
        book.getCategory().stream().map(bookHasCategory ->bookHasCategory.getCategory().getName()).forEach(joiner::add);
        return BookDetailResponse.builder()
                        .id(book.getId())
                        .authorName(book.getAuthor().getFullName())
                        .authorId(book.getAuthor().getId())
                        .title(book.getTitle())
                        .isbn(book.getIsbn())
                        .description(book.getDescription())
                        .price(book.getPrice())
                        .chapter(book.getChapters())
                        .thumbnail(book.getThumbnail())
                        .bookPath(book.getBookPath())
                        .createdAt(book.getCreatedAt())
                        .updatedAt(book.getUpdatedAt())
                        .category(joiner.toString())
                .build();
    }
}
