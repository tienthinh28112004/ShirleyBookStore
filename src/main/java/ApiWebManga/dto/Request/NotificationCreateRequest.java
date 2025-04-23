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

    private Long userReceiverId;

    private String title;

    private String message;

    private String url;
}
