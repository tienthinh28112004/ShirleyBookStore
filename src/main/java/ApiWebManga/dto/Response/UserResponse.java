package ApiWebManga.dto.Response;

import ApiWebManga.Entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    private Long id;
    private String fullName;
    private int facebookAccountId;
    private int googleAccountId;
    private String email;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    public static UserResponse convert(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .facebookAccountId(user.getFacebookAccountId())
                .googleAccountId(user.getGoogleAccountId())
                .email(user.getEmail())
                .birthday(user.getBirthday())
                .build();
    }
}