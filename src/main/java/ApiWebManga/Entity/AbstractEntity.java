package ApiWebManga.Entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@MappedSuperclass//đánh dấu đây là lớp để kế thừa
public abstract class AbstractEntity<T> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    T id;

    @Column(name ="created_at")
    private LocalDateTime createAt;

    @Column(name="updated_at")
    private LocalDateTime updateAt;

    @PrePersist//anotation này giúp tự động cập nhật
    protected void onCreate(){
        createAt =LocalDateTime.now();
        updateAt =LocalDateTime.now();
    }

    @PreUpdate//anotation này giúp tự động cập nhật mỗi khi có thay đổi
    protected void onUpdate(){
        updateAt =LocalDateTime.now();
    }
}
