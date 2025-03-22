package ApiWebManga.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "favorite_stories")
public class Favorite extends AbstractEntity<Long>{
    @Column(name="user_id",nullable = false)
    private Long userId;

    @Column(name="book_id",nullable = false)
    private Long bookId;
}
