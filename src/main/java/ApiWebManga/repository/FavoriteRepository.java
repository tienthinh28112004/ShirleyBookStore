package ApiWebManga.repository;

import ApiWebManga.Entity.Book;
import ApiWebManga.Entity.Favorite;
import ApiWebManga.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite,Long> {

    boolean existsByUserIdAndBookId(Long userId, Long bookId);

    void deleteByUserIdAndBookId(Long userId, Long bookId);
    @Query("SELECT f.bookId FROM Favorite f WHERE f.userId = :userId")
    List<Long> findBookIdsByUserId(@Param("userId") Long userId);//userId trong param ở đây tương ứng với :userId trong csdl
    //lấy ra danh sách id truyện mà người đăng nhập thích
}
