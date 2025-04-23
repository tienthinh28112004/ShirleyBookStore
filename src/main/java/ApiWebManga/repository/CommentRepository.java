package ApiWebManga.repository;

import ApiWebManga.Entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByUserIdAndBookId(@Param("userId") Long userId,
                                        @Param("bookId") Long bookId);
    List<Comment> findByBookId(@Param("bookId") Long bookId);

    List<Comment> findByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("SELECT c FROM Comment c WHERE c.book.Id = :bookId AND c.parentComment IS NULL")
    Page<Comment> findCommentByBookIdAndParentCommentIsNull(@Param("bookId") Long bookId, Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE c.book.Id = :bookId")
    List<Comment> findCommentByBookId(@Param("bookId") Long bookId);
}
