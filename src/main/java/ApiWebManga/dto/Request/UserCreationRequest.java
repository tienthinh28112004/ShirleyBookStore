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
public class UserCreationRequest implements Serializable {
    //id,fullname,password,email,dob,role
    @NotBlank(message = "firsName must be not blank")
    @MinSizeValidator(min=1)
    String fullName;

    @NotBlank(message = "email must be not blank")
    @MinSizeValidator(min=10)
    @Email(message = "invalid email")
    String email;

    @MinSizeValidator(min=8)
    @CustomPasswordValidator//thông điệp để trong dob
    String password;
}
