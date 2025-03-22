package ApiWebManga.dto.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailRequest {
    @NotNull(message = "BookId cannot be null")
    @Min(value = 1,message = "BookId must be greater than 0")
    private Long BookId;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1,message = "Quantity must be greater than 0")
    private Long quantity;
}
