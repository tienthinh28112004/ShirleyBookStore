package ApiWebManga.dto.Request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IntrospectRequest {
    private String accessToken;
}
