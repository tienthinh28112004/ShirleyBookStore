package ApiWebManga.repository;

import ApiWebManga.Entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByUserIdAndBookId(@Param("userId") Long userId,
                                        @Param("bookId") Long bookId);
    List<Comment> findByBookId(@Param("bookId") Long bookId);

    List<Comment> findByUserIdOrderByCreateAtDesc(Long userId);
}
