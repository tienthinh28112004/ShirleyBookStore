package ApiWebManga.dto.Response;

import ApiWebManga.Entity.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {
    private Long id;

    private Long bookId;

    private String name;

    private String avatar;

    private String content;

    private String elapsed;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    List<CommentResponse> replies = new ArrayList<>();
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
                .avatar(comment.getUser().getAvatarUrl())
                .bookId(comment.getBook().getId())
                .content(comment.getContent())
                .name(comment.getUser().getFullName())
                .elapsed(getTimeElapsed(comment.getCreatedAt()))
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .replies(comment.getReplies()!=null?
                        comment.getReplies().stream().map(CommentResponse::convert)
                        .collect(Collectors.toList()) : new ArrayList<>())
                .build();
    }
}
