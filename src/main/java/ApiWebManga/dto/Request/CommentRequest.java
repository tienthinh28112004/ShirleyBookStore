package ApiWebManga.dto.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest implements Serializable {
    @NotBlank(message = "content cannot be null")
    private String content;

    private Long parentCommentId;

    private Long bookId;

}
