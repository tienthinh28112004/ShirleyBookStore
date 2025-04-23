package ApiWebManga.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "books")
public class Book extends AbstractEntity<Long> {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "isbn", nullable = false, unique = true)
    private String isbn;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "stock")
    private Long stock;

    @Column(name = "publisher", nullable = false)
    private String publisher;

    @Column(name = "thumbnail", nullable = false, columnDefinition = "TEXT")
    private String thumbnail;

    @Column(name = "book_path", columnDefinition = "TEXT")
    private String bookPath;

    @Column(name = "view")
    @ColumnDefault("0")
    private Long views;

    @Column(name = "published_date")
    private LocalDate publishedDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User author;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<BookHasCategory> category=new ArrayList<>();

    @OneToMany(mappedBy = "book",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference//được đặt ở phía cha khi chuyển thành json sẽ được hiển thị trong json tránh được vòng lặp vô hạn
    private List<Chapter> chapters =new ArrayList<>();

    @OneToMany(mappedBy = "book",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Comment> comments=new ArrayList<>();
}
