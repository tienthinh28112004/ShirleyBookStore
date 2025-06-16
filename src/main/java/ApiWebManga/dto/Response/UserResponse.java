package ApiWebManga.dto.Response;

import ApiWebManga.Entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    private Long id;
    private String fullName;
    private String avatarUrl;
    private String email;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    //private CartTotalResponse cart;
    private boolean isActive;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private List<String> role;
    public static UserResponse convert(User user) {
        List<String> role=new ArrayList<>();
        user.getUserHasRoles().stream().map(userHasRoles -> userHasRoles.getRole().getName()).forEach(role::add);
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .isActive(user.isActive())
                .birthday(user.getBirthday())
                .avatarUrl(user.getAvatarUrl())
                //.cart(CartTotalResponse.convert(user.getCart()))
                .createdAt(user.getCreatedAt())
                .phoneNumber(user.getPhoneNumber())
                .role(role)
                .build();
    }
}