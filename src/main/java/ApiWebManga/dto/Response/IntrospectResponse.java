package ApiWebManga.dto.Response;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IntrospectResponse {
    private boolean valid;
    private List<String> scope;
}
