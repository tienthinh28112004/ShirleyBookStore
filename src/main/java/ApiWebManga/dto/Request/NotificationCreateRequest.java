package ApiWebManga.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationCreateRequest {
    private Long userId;

    private Long bookId;

    private Long chapterId;

    private String message;

    private String link;//dùng để chuyển hướng đến trang khác
}
