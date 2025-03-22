package ApiWebManga.dto.Response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
public class SignInResponse implements Serializable {
    private Long userId;

    private String accessToken;

    private String refreshToken;

    private Long accessTokenExp;//thời gian hết hạn của token

    private Long refreshTokenExp;//thời gian hết han của refreshToken
    private List<String> roleList;
}
