package ApiWebManga.Controller;

import ApiWebManga.dto.Request.ApiResponse;
import ApiWebManga.dto.Request.BookCreationRequest;
import ApiWebManga.dto.Response.BookDetailResponse;
import ApiWebManga.dto.Response.PageResponse;
import ApiWebManga.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/book")
public class BookController {
    private final BookService bookService;

    @PostAuthorize("ADMIN")
    @Operation(summary = "upload book", description = "upload book with bookCreationRequest,thumbnail,bookPdf")
    @PostMapping(value = "/upload-books")
    public ApiResponse<?> uploadBook(@RequestPart(name = "request") BookCreationRequest request,//json
                                     @RequestPart(name = "thumbnail") MultipartFile thumbnail,//file
                                     @RequestPart(name = "book-pdf", required = false) MultipartFile bookPdf) {
        log.info("upload book successfully");
        log.info("abcd");
        return ApiResponse.<BookDetailResponse>builder()
                .message("upload book successfully")
                .result(bookService.uploadBook(request,thumbnail,bookPdf))
                .build();
    }
    @PostAuthorize("USER")
    @Operation(summary = "getBookById", description = "information book by id")
    @GetMapping("/getBook/{id}")
    public ApiResponse<?> getBookById(@PathVariable Long id) {
        return ApiResponse.<BookDetailResponse>builder()
                .message("information book succesfully")
                .result(bookService.getBookId(id))
                .build();
    }
    //có sự khác bieetj giữa ấy ra danh sách user và lấy ra danh sách sách
    @PostAuthorize("USER")
    @Operation(summary = "listBook", description = "information list book wiht page,size")
    @GetMapping("/booklist")
    public ApiResponse<?> getAll(@RequestParam(required = false,defaultValue = "1") int page,
                                 @RequestParam(required = false,defaultValue = "10") int size
    ) {
        return ApiResponse.<PageResponse<List<BookDetailResponse>>>builder()
                .message("information book succesfully")
                .result(bookService.getAllBook(page,size))
                .build();
    }

    @PostAuthorize("USER")
    @Operation(summary = "listBook by criteria", description = "information list book search,sort by criteria ")
    @GetMapping("/book-search-criteria")
    public ApiResponse<?> getAllBookSearchCriteria(
            @RequestParam(required = false,defaultValue = "1") int page,
            @RequestParam(required = false,defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String user,
            @RequestParam(required = false) String... search
    ) {
        return ApiResponse.<PageResponse<List<BookDetailResponse>>>builder()
                .message("list book search criteria")
                .result(bookService.getBoolWithSortAndMultiFieldAndSearch(page, size, sortBy, user, search))
                .build();
    }

    @PostAuthorize("USER")
    @Operation(summary = "search by keyword", description = "information list book search criteria ")
    @GetMapping("/book-search-keyword")
    public ApiResponse<?> getAllBookSearchCriteria(
            @RequestParam(required = false,defaultValue = "1") int page,
            @RequestParam(required = false,defaultValue = "10") int size,
            @RequestParam(required = false) String keyword
    ) {
        return ApiResponse.<PageResponse<List<BookDetailResponse>>>builder()
                .message("book search keyword")
                .result(bookService.getBookWithSortAndSearchByKeyword(page, size,keyword))
                .build();
    }

}
