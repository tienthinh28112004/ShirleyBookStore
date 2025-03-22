package ApiWebManga.repository;

import ApiWebManga.Entity.Notification;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    @Query("SELECT DISTINCT n FROM Notification n WHERE n.userId= :userId AND n.bookId= :bookId AND n.isEnabled=true ORDER BY n.createAt DESC LIMIT 1")
    Notification findByUserIdAndBookId(@Param("userId") Long userId,@Param("bookId") Long bookId);

    @Query("SELECT n FROM Notification n WHERE n.userId= :userId  ORDER BY n.createAt DESC")
    List<Notification> findRecentByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT DISTINCT n.bookId FROM Notification n WHERE n.userId= :userId")
    List<Long> findBookIdsByUserId(@Param("userId") Long userId);
    @Query("SELECT n.userId FROM Notification n WHERE n.bookId= :bookId")
    List<Long> findUserIdsByBookId(@Param("bookId") Long bookId);

}
