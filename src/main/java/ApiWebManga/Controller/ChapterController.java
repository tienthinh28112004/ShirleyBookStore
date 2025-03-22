package ApiWebManga.Controller;

import ApiWebManga.Entity.Category;
import ApiWebManga.Entity.Chapter;
import ApiWebManga.dto.Request.ApiResponse;
import ApiWebManga.dto.Request.ChapterCreateRequest;
import ApiWebManga.dto.Response.BookDetailResponse;
import ApiWebManga.dto.Response.ChapterResponse;
import ApiWebManga.service.ChapterService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/chapter")
public class ChapterController {
//    String upLoadChapter(ChapterCreateRequest request, MultipartFile chapterPdf);
//    Chapter getChapterById(Long chapterId);
//    Chapter findPrevChapter(Long bookId,Long chapterId);
//    Chapter findNextChapter(Long bookId,Long chapterId);
//    List<Chapter> findChaptersByBookId(Long bookId);
//    List<ChapterResponse> getRecentChaptersWithElapsedTime();
//    ChapterResponse getLatestChapterAndTime(Long bookId);
//    List<ChapterResponse> getRecentChapterByBookWithElapsedTime(Long bookId);
    private final ChapterService chapterService;
    @PostAuthorize("ADMIN")
    @PostMapping(value = "/upLoadChapter")
    @Operation(summary = "upload chapter")
    public ApiResponse<BookDetailResponse> upLoadChapter (
            @RequestPart @Valid ChapterCreateRequest request,
            @RequestPart MultipartFile chapterPdf) {
    return ApiResponse.<BookDetailResponse>builder()
            .message("upload successfully")
            .result(chapterService.upLoadChapter(request,chapterPdf))
            .build();
    }

    @PostAuthorize("USER")
    @GetMapping(value = "/getChapterById/{chapterId}")
    @Operation(summary = "information chapter by id")
    public ApiResponse<Chapter> getChapterById (@PathVariable Long chapterId) {
        return ApiResponse.<Chapter>builder()
                .message("get chapter by id successfully")
                .result(chapterService.getChapterById(chapterId))
                .build();
    }

    @PostAuthorize("USER")
    @GetMapping(value = "/findPrevChapter/{bookId}/{chapterId}")//lấy chapter trước so với chapter trong sách
    @Operation(summary = "find prev chapter")
    public ApiResponse<Chapter> findPrevChapter (@PathVariable Long bookId,@PathVariable Long chapterId) {
        return ApiResponse.<Chapter>builder()
                .message("find prev chapter successfully")
                .result(chapterService.findPrevChapter(bookId, chapterId))
                .build();
    }
    @PostAuthorize("USER")
    @GetMapping(value = "/findNextChapter/{bookId}/{chapterId}")//lấy chapter trước so với chapter trong sách
    @Operation(summary = "find next chapter")
    public ApiResponse<Chapter> findNextChapter (@PathVariable Long bookId,@PathVariable Long chapterId) {
        return ApiResponse.<Chapter>builder()
                .message("find next chapter successfully")
                .result(chapterService.findNextChapter(bookId, chapterId))
                .build();
    }

    @PostAuthorize("USER")
    @GetMapping(value = "/findChaptersByBookId/{bookId}")
    @Operation(summary = "list chapter by book")
    public ApiResponse<List<Chapter>> findChaptersByBookId (@PathVariable Long bookId) {
        return ApiResponse.<List<Chapter>>builder()
                .message("list chapter successfully")
                .result(chapterService.findChaptersByBookId(bookId))
                .build();
    }

    @PostAuthorize("USER")
    @GetMapping(value = "/getRecentChaptersWithElapsedTime")//lấy ra các chương mới nhất theo ngày tạo(tất cả các quyển)
    @Operation(summary = "Get Recent Chapter With Elapsed Time")
    public ApiResponse<List<ChapterResponse>> getRecentChaptersWithElapsedTime () {
        return ApiResponse.<List<ChapterResponse>>builder()
                .message("Get Recent Chapter With Elapsed Time")
                .result(chapterService.getRecentChaptersWithElapsedTime())
                .build();
    }
    @PostAuthorize("USER")
    @GetMapping(value = "/getLatestChapterAndTime/{bookId}")//lấy ra các chương mới nhất theo ngày tạo ứng với mỗi quyển sách
    @Operation(summary = "Get Latest Chapter And Time")
    public ApiResponse<ChapterResponse> getLatestChapterAndTime (@PathVariable Long bookId) {
        return ApiResponse.<ChapterResponse>builder()
                .message("Get Latest Chapter And Time")
                .result(chapterService.getLatestChapterAndTime(bookId))
                .build();
    }

    @PostAuthorize("USER")
    @GetMapping(value = "/getRecentChapterByBookWithElapsedTime/{bookId}")//lấy ra danh sách các chương mới nhất theo ngày tạo ứng với quyển sách
    @Operation(summary = "Get Recent Chapter By Book With Elapsed Time")
    public ApiResponse<List<ChapterResponse>> getRecentChapterByBookWithElapsedTime (@PathVariable Long bookId) {
        return ApiResponse.<List<ChapterResponse>>builder()
                .message("Get Recent Chapter By Book With Elapsed Time")
                .result(chapterService.getRecentChapterByBookWithElapsedTime(bookId))
                .build();
    }
}
