package ApiWebManga.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comments")
public class Comment{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="book_id")
    @JsonBackReference//đặt ở phía con và khi chuyển đối tượng sang json sẽ bị bỏ qua để tránh vòng lặp vô hạn
    private Book book;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)//chỉ ra khi bị gọi
    @JoinColumn(name = "parent_comment_id")
    Comment parentComment;

    @OneToMany(mappedBy = "parentComment",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    List<Comment> replies;
}
