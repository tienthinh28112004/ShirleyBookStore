package ApiWebManga.dto.Response;

import ApiWebManga.Entity.User;
import ApiWebManga.Enums.RegistrationStatus;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterAuthorResponse {
    private Long id;
    private String email;
    private String name;
    private String phone;
    private String expertise;
    private Double yearsOfExperience;
    private String bio;
    private String facebookLink;
    private String certificate;
    private String cvUrl;
    private RegistrationStatus registrationStatus;

    public static UserRegisterAuthorResponse convert(User user){
        return UserRegisterAuthorResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getFullName())
                .phone(user.getPhoneNumber())
                .expertise(user.getExpertise())
                .yearsOfExperience(user.getYearsOfExperience())
                .bio(user.getBio())
                .facebookLink(user.getFacebookLink())
                .certificate(user.getCertificate())
                .cvUrl(user.getCvUrl())
                .registrationStatus(user.getRegistrationStatus())
                .build();
    }
}
