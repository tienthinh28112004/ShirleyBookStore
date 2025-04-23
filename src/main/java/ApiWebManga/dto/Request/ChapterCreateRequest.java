package ApiWebManga.dto.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChapterCreateRequest {
    @NotBlank(message = "Title Chapter cannot be null")
    private String title;

    @NotBlank(message = "Content Chapter cannot be null")
    private String content;

    private Long bookId;
}
