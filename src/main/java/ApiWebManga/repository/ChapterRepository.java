package ApiWebManga.repository;

import ApiWebManga.Entity.Chapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ChapterRepository extends JpaRepository<Chapter,Long> {
    List<Chapter> findByBookId(Long bookId);
    //lấy ra chapter gần nhất trong quyển truyện so với chapter hiện tại
    @Query("SELECT c FROM Chapter c WHERE c.book.id = :bookId AND c.id < :chapterId ORDER BY c.id DESC")
    List<Chapter> findPrevChapter(@Param("bookId") Long bookId, @Param("chapterId") Long chapterId);
    //lấy ra chapter mới nhất trong quyển truyện so với chapter hiện tại
    @Query("SELECT c FROM Chapter c WHERE c.book.id=:bookId AND c.id >:chapterId ORDER BY c.id ASC")
    List<Chapter> findNextChapter(@Param("bookId") Long bookId,@Param("chapterId") Long chapterId);
    //Lấy 10 chương mới nhất theo ngày tạo
    List<Chapter> findTop10ByOrderByCreatedDateDesc();
    //Lấy tất cả chương của 1 truyện cụ thể và sắp xếp thoe ngày tạo
    List<Chapter> findByBookIdOrderByCreatedDateDesc(Long bookId);
    //Lấy chương mới nhất của 1 truyện
    Chapter findTopByBookIdOrderByCreatedDateDesc(Long bookId);
    //Lấy chương mới nhất của từng truyện
    @Query("SELECT c FROM Chapter c WHERE c.id IN ("+
        "SELECT MAX(ch.id) FROM Chapter ch WHERE ch.book.id = c.book.id GROUP BY ch.book.id) "+
        "ORDER BY c.createdDate DESC")
    Page<Chapter> findLatestChaptersOfEachBook(Pageable pageable);

}
