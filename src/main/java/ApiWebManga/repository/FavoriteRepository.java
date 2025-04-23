package ApiWebManga.repository;

import ApiWebManga.Entity.Book;
import ApiWebManga.Entity.Favorite;
import ApiWebManga.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite,Long> {

    void deleteByUserIdAndBookId(Long userId, Long bookId);
    @Query("SELECT f.book.id FROM Favorite f WHERE f.user.id = :userId")
    List<Long> findBookIdsByUserId(@Param("userId") Long userId);//userId trong param ở đây tương ứng với :userId trong csdl
    boolean existsByUserAndBook(User user, Book book);//nó sẽ tự động ánh xạ vào id của entity để so sánh
    Page<Favorite> findByUser(User user, Pageable pageable);//nó sẽ tự động ánh xạ đến userId(hàm lấy ra danh sách yêu thích của user)

    List<Favorite> findByUser(User user);
    //lấy ra danh sách id truyện mà người đăng nhập thích
}
