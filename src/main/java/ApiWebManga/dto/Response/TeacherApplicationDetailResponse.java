package ApiWebManga.dto.Response;

import ApiWebManga.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.StringJoiner;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherApplicationDetailResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String avatar;
    private LocalDate dob;               // Giữ nguyên kiểu LocalDate
    private String cvUrl;
    private String certificate;
    private String facebookLink;
    private String description;
    private Double yearsOfExperience;
    private String role;
    public static TeacherApplicationDetailResponse convert(User user){
        StringJoiner joiner = new StringJoiner(",");
        user.getUserHasRoles().stream().map(userHasRoles -> userHasRoles.getRole().getName()).forEach(joiner::add);
        return TeacherApplicationDetailResponse.builder()
                .id(user.getId())
                .name(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhoneNumber())
                .avatar(user.getAvatarUrl())
                .dob(user.getBirthday())
                .cvUrl(user.getCvUrl())
                .certificate(user.getCertificate())
                .facebookLink(user.getFacebookLink())
                .description(user.getBio())
                .yearsOfExperience(user.getYearsOfExperience())
                .role(joiner.toString())
                .build();
    }
}


