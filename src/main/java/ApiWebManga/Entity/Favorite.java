package ApiWebManga.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "favorites")
@JsonIgnoreProperties({"user","course"})//khi chuyển đối tượng sang json các thuộc tính nh user hay course sẽ được bỏ qua
public class Favorite extends AbstractEntity<Long>{

    @ManyToOne(fetch = FetchType.LAZY,cascade = {
            CascadeType.PERSIST,CascadeType.MERGE,
            CascadeType.DETACH,CascadeType.REFRESH})
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {
            CascadeType.PERSIST,CascadeType.MERGE,
            CascadeType.DETACH,CascadeType.REFRESH})
    @JoinColumn(name = "book_id",nullable = false)
    private Book book;
    //Ở đây sử dụng Cascade mà không sử dụng Cascade.ALL ở đâyvifi nếu dùng CascadeAll
    //thì nó sẽ bao gồm cả Cascade.REMOVE mà nếu có Cascade.REMOVE ở đây thì
    //nó khi xóa Favorite nó sẽ xóa lun cả các User vaf Book liên kết nên ở đây phải tránh dùng
}
