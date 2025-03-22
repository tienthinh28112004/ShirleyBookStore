package ApiWebManga.Controller;

import ApiWebManga.dto.Request.ApiResponse;
import ApiWebManga.dto.Request.CommentRequest;
import ApiWebManga.dto.Response.CommentResponse;
import ApiWebManga.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/comment")
public class CommentController {
//    CommentResponse insertComment(CommentRequest request);
//    void deleteComment(Long commentId);
//    void updateComment(Long CommentId, CommentUpdateRequest request);
//    List<CommentResponse> getCommentsByUserAndBook(Long userId, Long bookId);
//    List<CommentResponse> getCommentsByBook(Long bookId);
//    List<CommentResponse> getUserComments(Long userId);
    //thiếu update comment nhưng sẽ làm sau:)))
    private final CommentService commentService;
    @PostAuthorize("USER")
    @PostMapping(value = "/insertComment")
    @Operation(summary = "create comment")
    public ApiResponse<CommentResponse> insertComment (@RequestBody @Valid CommentRequest request) {
        return ApiResponse.<CommentResponse>builder()
                .message("Comment successfully")
                .result(commentService.insertComment(request))
                .build();
    }
    @PostAuthorize("USER")
    @DeleteMapping(value = "/deleteComment/{commentId}")
    @Operation(summary = "delete comment")
    public ApiResponse<Void> deleteComment (@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ApiResponse.<Void>builder()
                .message("delete successfully")
                .build();
    }
    @PostAuthorize("ADMIN")
    @GetMapping(value = "/getCommentsByUserAndBook/{userId}/{bookId}")
    @Operation(summary = "comment by user and book")
    public ApiResponse<List<CommentResponse>> commentUserAndBook (@PathVariable Long userId,@PathVariable Long bookId) {
        return ApiResponse.<List<CommentResponse>>builder()
                .message("successfully")
                .result(commentService.getCommentsByUserAndBook(userId,bookId))
                .build();
    }
    @PostAuthorize("USER")
    @GetMapping(value = "/getCommentsByBook/{bookId}")
    @Operation(summary = "Comment by book")
    public ApiResponse<List<CommentResponse>> commentByBook (@PathVariable Long bookId) {
        return ApiResponse.<List<CommentResponse>>builder()
                .message("successfully")
                .result(commentService.getCommentsByBook(bookId))
                .build();
    }
    @PostAuthorize("ADMIN")
    @GetMapping(value = "/getUserComments/{userId}")
    @Operation(summary = "Comment by user")
    public ApiResponse<List<CommentResponse>> commentByUser (@PathVariable Long userId) {
        return ApiResponse.<List<CommentResponse>>builder()
                .message("successfully")
                .result(commentService.getUserComments(userId))
                .build();
    }
}
