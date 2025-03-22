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

    private Long userId;

    private Long bookId;//trả về bookId cũng được,gán nó vào thẻ link nếu người dùng abams vào thì sẽ link sang truyện

    private String message;

    private String elapsedTime;//thời gian trôi qua

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
    public static NotificationResponse convert(Notification notification){
        return NotificationResponse.builder()
                .id(notification.getId())
                .bookId(notification.getBookId())
                .userId(notification.getUserId())
                .elapsedTime(getTimeElapsed(notification.getCreateAt()))
                .build();
    }
}
