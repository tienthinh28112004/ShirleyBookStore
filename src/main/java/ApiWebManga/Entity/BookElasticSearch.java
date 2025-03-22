package ApiWebManga.Entity;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;


@Document(indexName = "book")//lưu vào document với index là book
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BookElasticSearch implements Serializable {
    //chú ý:nếu class này thay đổi trong tương lai thì cần tạo serialVersionUID(tự tra mạng sẽ hiểu::)
    @Id
    private Long id;

    @Field(name="title",type = FieldType.Text)
    //đánh dấu là 1 trường trong elasticsearch,nó sẽ được lưu trữ và phân tích từ khóa để hỗ tợ tìm kiếm toàn văn
    private String title;

    @Field(name="isbn",type = FieldType.Text)
    private String isbn;

    @Field(name="description",type = FieldType.Text)
    private String description;

    @Field(name = "author_name",type = FieldType.Text)
    private String authorName;

    @Field(name="price",type = FieldType.Long)
    private Long price;
}
