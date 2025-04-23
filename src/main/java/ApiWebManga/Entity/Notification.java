package ApiWebManga.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notifications")//đánh dấu chỉ tồn tại duy nhất 1 cặp userId và bookId
public class Notification extends AbstractEntity<Long>{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    @JsonIgnore//biến này sẽ không được truy cập khi trả ra json nên vẫn có thể gọi notification để lấy dữ liệu
    private User user;

    @Column(name = "title")
    private String title;

    @Column(name = "message")
    private String message;

    @Column(name = "is_read")
    private Boolean isRead;

    @Column(name = "url")
    private String url;

    @Column(name = "avatarUrl")
    private String avatarUrl;
}
