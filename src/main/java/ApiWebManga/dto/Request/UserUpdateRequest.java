package ApiWebManga.dto.Request;

import ApiWebManga.validator.DobValidator;
import ApiWebManga.validator.MinSizeValidator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {

    @MinSizeValidator(min=8)
    String phoneNumber;

    @DobValidator(min= 12)//lớn hơn 12 tuổi mới được vào
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate dob;
}
