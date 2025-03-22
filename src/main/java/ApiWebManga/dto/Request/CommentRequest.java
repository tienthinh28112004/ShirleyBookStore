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
    @NotBlank(message = "bookId cannot be null")
    private Long bookId;

    @NotBlank(message = "userId cannot be null")
    private Long userId;

    @NotBlank(message = "content cannot be null")
    private String content;
}
