package ApiWebManga.dto.Response;

import ApiWebManga.Entity.Comment;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {
    private Long id;

    private String content;

    private UserResponse userResponse;//thông tin user(chỉ lấy được fullname là hưu ích thôi)

    private String elapsed;

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
    public static CommentResponse convert(Comment comment){
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .userResponse(UserResponse.convert(comment.getUser()))
                .elapsed(getTimeElapsed(comment.getCreateAt()))
                .build();
    }
}
