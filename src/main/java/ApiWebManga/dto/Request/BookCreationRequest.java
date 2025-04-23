package ApiWebManga.dto.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
public class BookCreationRequest implements Serializable {
    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "Isbn cannot be blank")
    private String isbn;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotBlank(message = "Price cannot be blank")
    @Min(value = 1,message = "Price must be greater than 0")
    private Long price;

    @NotBlank(message = "category cannot be blank")
    private List<Long> categoriesId;

}
