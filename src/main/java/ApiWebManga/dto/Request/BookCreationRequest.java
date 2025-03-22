package ApiWebManga.dto.Request;

import ApiWebManga.Entity.BookHasCategory;
import ApiWebManga.Entity.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.math.BigDecimal;
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

    @NotBlank(message = "Language cannot be blank")
    private String language;

    @NotBlank(message = "category cannot be blank")
    private List<String> categories;

}
