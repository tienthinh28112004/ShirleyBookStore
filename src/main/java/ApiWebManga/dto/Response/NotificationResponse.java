package ApiWebManga.dto.Response;

import ApiWebManga.Entity.Notification;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationResponse {
    private Long id;

    private String title;

    private String message;

    private Boolean isRead;

    private String url;

    private String avatarUrl;

    private String elapsed;

    public static NotificationResponse convert(Notification notification){
        return NotificationResponse.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .isRead(notification.getIsRead())
                .url(notification.getUrl())
                .avatarUrl(notification.getAvatarUrl())
                .elapsed(getTimeElapsed(notification.getCreatedAt()))
                .build();
    }

    public static String getTimeElapsed(LocalDateTime createDate){
        if(createDate == null){
            return "không có thông tin";
        }
        Duration duration=Duration.between(createDate,LocalDateTime.now());
        if(duration.toMinutes() < 60){
            return duration.toMinutes()+" phút trước";
        }else if(duration.toHours() < 24){
            return duration.toHours()+" giờ trước";
        }else if(duration.toDays() < 30){
            return duration.toDays()+" ngày trước";
        }else{
            return (duration.toDays() / 30) +" tháng trước";
        }
    }
}
