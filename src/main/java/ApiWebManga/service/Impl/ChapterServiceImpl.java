package ApiWebManga.service.Impl;

import ApiWebManga.Entity.Book;
import ApiWebManga.Entity.Chapter;
import ApiWebManga.Exception.NotFoundException;
import ApiWebManga.dto.Request.BookCreationRequest;
import ApiWebManga.dto.Request.ChapterCreateRequest;
import ApiWebManga.dto.Response.BookDetailResponse;
import ApiWebManga.dto.Response.ChapterResponse;
import ApiWebManga.repository.BookRepository;
import ApiWebManga.repository.ChapterRepository;
import ApiWebManga.service.ChapterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChapterServiceImpl implements ChapterService {
    private final ChapterRepository chapterRepository;
    private final BookRepository bookRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public BookDetailResponse upLoadChapter(ChapterCreateRequest request,
                                            MultipartFile chapterPdf) {//nội dung chính của truyện
        Book book=bookRepository.findById(request.getBookId())
                .orElseThrow(()->new NotFoundException("Book not found"));
        String chapterUrl=null;
        if(chapterPdf !=null){
            chapterUrl=cloudinaryService.uploadImage(chapterPdf);
        }
        Chapter chapter=Chapter.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .chapterPath(chapterUrl)
                .book(book)
                .build();
        chapterRepository.save(chapter);
        book.getChapters().add(chapter);//lưu thông tin chapter vào cái list trong book
        return BookDetailResponse.convert(book);
        //return "tạo chapter thành công,chưa test nhưng tạm vậy đã";
    }

    @Override
    public Chapter getChapterById(Long chapterId) {
        return chapterRepository.findById(chapterId)
                .orElseThrow(()->new NotFoundException("Chapter not found"));
    }
    @Override//lấy ra chương gần nhất trong truyện so với chương hiện tại
    public Chapter findPrevChapter(Long bookId, Long chapterId) {
        List<Chapter> chapters = chapterRepository.findPrevChapter(bookId,chapterId);
        return (chapters.isEmpty()) ? null:chapters.getFirst();//lấy ra chung đầu tiên trong danh sách lấy được
    }

    @Override//lấy ra chapter tiếp theo trong quyển truyện
    public Chapter findNextChapter(Long bookId, Long chapterId) {
        List<Chapter> chapters=chapterRepository.findNextChapter(bookId, chapterId);
        return (chapters.isEmpty()) ? null:chapters.getFirst();//lấy ra chương đầu tiên trong dnah sách lấy được
    }
    @Override
    public List<Chapter> findChaptersByBookId(Long bookId) {
        return chapterRepository.findByBookId(bookId);
    }

    @Override
    public List<ChapterResponse> getRecentChaptersWithElapsedTime() {
        Pageable pageable= PageRequest.of(0,10);
        Page<Chapter> chapters = chapterRepository.findLatestChaptersOfEachBook(pageable);
        return chapters.stream().map(chapter -> ChapterResponse.builder()
                .id(chapter.getId())//id của truyện
                .titleLatestChapter(chapter.getTitle())//tiêu đề của chap mới nhất của truyện
                .titleBook(chapter.getBook().getTitle())//lấy ra tiêu đề của book được kaays ả
                .elapsedTime(getTimeElapsed(chapter.getCreatedDate()))
                .chapterPath(chapter.getChapterPath())
                .build()).collect(Collectors.toList());
    }

    @Override
    public ChapterResponse getLatestChapterAndTime(Long bookId) {
        //lấy ra chương mới nhất của scahs theo ngày tạo
        Chapter latestChapter = chapterRepository.findTopByBookIdOrderByCreatedDateDesc(bookId);
        if(latestChapter!=null){
            return ChapterResponse.builder()
                    .id(latestChapter.getId())//lấy ra ifd của chap mới nhất
                    .titleLatestChapter(latestChapter.getTitle())//lấy ra tiêu đề của chap mới nhất
                    .titleBook(latestChapter.getBook().getTitle())//lấy ra tiêu đề của truyện
                    .elapsedTime(getTimeElapsed(latestChapter.getCreatedDate()))//lấy ra thời gian cách với hiện tại
                    .chapterPath(latestChapter.getChapterPath())
                    .build();
        }
        return null;
    }

    @Override
    public List<ChapterResponse> getRecentChapterByBookWithElapsedTime(Long bookId) {
        //lấy ra danh sách các chương mới nhất của 1 truyện cụ thể;
        List<Chapter> chapters=chapterRepository.findByBookIdOrderByCreatedDateDesc(bookId);
        return chapters.stream().map(chapter -> ChapterResponse.builder()
                .id(chapter.getId())//id của truyện
                .titleLatestChapter(chapter.getTitle())//tiêu đề của chap mới nhất của truyện
                .titleBook(chapter.getBook().getTitle())//lấy ra tiêu đề của book được kaays ả
                .elapsedTime(getTimeElapsed(chapter.getCreatedDate()))
                .chapterPath(chapter.getChapterPath())
                .build()).collect(Collectors.toList());
    }

    public String getTimeElapsed(LocalDateTime createDate){
        if(createDate == null){
            return "không có thông tin";
        }
        Duration duration=Duration.between(createDate,LocalDateTime.now());
        if(duration.toMinutes() < 60){
            return duration.toMinutes()+" phút trước";
        }else if(duration.toHours() < 24){
            return duration.toHours()+" giờ trước";
        }else if(duration.toDays() < 30){
            return duration.toDays()+" ngày trước";
        }else{
            return (duration.toDays() / 30) +" tháng trước";
        }
    }
}
