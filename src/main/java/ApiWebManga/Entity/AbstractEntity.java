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
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @PrePersist//anotation này giúp tự động cập nhật
    protected void onCreate(){
        createdAt =LocalDateTime.now();
        updatedAt =LocalDateTime.now();
    }

    @PreUpdate//anotation này giúp tự động cập nhật mỗi khi có thay đổi
    protected void onUpdate(){
        updatedAt =LocalDateTime.now();
    }
}
