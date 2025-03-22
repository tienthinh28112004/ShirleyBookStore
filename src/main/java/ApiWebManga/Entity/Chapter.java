package ApiWebManga.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chapters")
public class Chapter{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name="content",nullable = false)
    private String content;

    @Column(columnDefinition = "LONGTEXT")
    private String chapterPath;

    private LocalDateTime createdDate;
    @PrePersist
    protected void onCreate(){
    if (this.createdDate == null) {
        this.createdDate = LocalDateTime.now();
        }
    }

    @ManyToOne
    @JoinColumn(name="book_id")
    @JsonBackReference//đặt ở phía con và khi chuyển đối tượng sang json sẽ bị bỏ qua để tránh vòng lặp vô hạn
    private Book book;

}
