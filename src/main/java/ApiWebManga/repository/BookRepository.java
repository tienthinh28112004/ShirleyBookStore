package ApiWebManga.repository;

import ApiWebManga.Entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book,Long> {
    @Query("SELECT b FROM Book b WHERE b.title LIKE %:keyword% OR b.description LIKE %:keyword%")
    Page<Book> findAllByKeyword(Pageable pageable, @Param("keyword") String keyword);

    @Query("SELECT b FROM Book b WHERE (b.title LIKE %:keyword% OR b.description LIKE %:keyword%) AND b.author.id = :userId")
    Page<Book> findAllByKeywordAndUserId(Pageable pageable,@Param("keyword") String keyword,@Param("userId") Long userId);

    @Query("SELECT b FROM Book b WHERE b.author.id=:userId")
    Page<Book> findAllByUserId(Pageable pageable,@Param("userId") Long userId);
}
