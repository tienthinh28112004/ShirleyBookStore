package ApiWebManga.dto.Response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChapterResponse {
    private Long id;//id của chap mới nhất
    private String titleLatestChapter;//tiêu đề chapter mới nhất
    private String titleBook;//tiêu đề của truyện tương ứng với chap mới nhất ấy
    private String elapsedTime;//thời gian trôi qua
    private String chapterPath;
}
