package ApiWebManga.dto.Request;

import ApiWebManga.validator.CustomPasswordValidator;
import ApiWebManga.validator.MinSizeValidator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest implements Serializable {
    @NotBlank(message = "email must be not blank")
    @MinSizeValidator(min=10)
    @Email(message = "invalid email")
    String email;

    @MinSizeValidator(min=8)
    @CustomPasswordValidator//thông điệp để trong dob
    String password;

}
