package ApiWebManga.dto.Request;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterAuthorRequest {
    private String email;
    private String name;
    private String phone;
    private String expertise;
    private Double yearsOfExperience;
    private String bio;
    private String facebookLink;
}
