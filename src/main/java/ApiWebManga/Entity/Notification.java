package ApiWebManga.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notification")//đánh dấu chỉ tồn tại duy nhất 1 cặp userId và bookId
public class Notification extends AbstractEntity<Long>{
    private Long userId;

    private Long bookId;

    private Long chapterId;

    private String message;

    private boolean isEnabled;

    private String elapsedTime;//thời gian trôi qua
}
