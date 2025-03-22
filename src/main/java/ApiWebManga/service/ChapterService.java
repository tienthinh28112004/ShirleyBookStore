package ApiWebManga.service;

import ApiWebManga.Entity.Chapter;
import ApiWebManga.dto.Request.ChapterCreateRequest;
import ApiWebManga.dto.Response.BookDetailResponse;
import ApiWebManga.dto.Response.ChapterResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ChapterService {
    BookDetailResponse upLoadChapter(ChapterCreateRequest request, MultipartFile chapterPdf);
    Chapter getChapterById(Long chapterId);
    Chapter findPrevChapter(Long bookId,Long chapterId);
    Chapter findNextChapter(Long bookId,Long chapterId);
    List<Chapter> findChaptersByBookId(Long bookId);
    List<ChapterResponse> getRecentChaptersWithElapsedTime();
    ChapterResponse getLatestChapterAndTime(Long bookId);
    List<ChapterResponse> getRecentChapterByBookWithElapsedTime(Long bookId);
}
