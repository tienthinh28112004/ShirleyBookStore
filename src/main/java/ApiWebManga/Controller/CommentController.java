package ApiWebManga.Controller;

import ApiWebManga.dto.Request.ApiResponse;
import ApiWebManga.dto.Request.CommentRequest;
import ApiWebManga.dto.Request.CommentUpdateRequest;
import ApiWebManga.dto.Response.CommentResponse;
import ApiWebManga.dto.Response.CommentUpdateResponse;
import ApiWebManga.dto.Response.PageResponse;
import ApiWebManga.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @GetMapping(value = "/getCommentsByBook/{bookId}")
    @Operation(summary = "Comment by book")
    public ApiResponse<PageResponse<List<CommentResponse>>> commentByBook (@PathVariable Long bookId,
                                       @RequestParam(defaultValue = "1", required = false) int page,
                                       @RequestParam(defaultValue = "10", required = false) int size) {
        return ApiResponse.<PageResponse<List<CommentResponse>>>builder()
                .message("successfully")
                .result(commentService.getCommentsByBook(bookId,page,size))
                .build();
    }
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(value = "/insertComment")
    @Operation(summary = "create comment")
    public ApiResponse<CommentResponse> insertComment (@RequestBody @Valid CommentRequest request) {
        return ApiResponse.<CommentResponse>builder()
                .message("Comment successfully")
                .result(commentService.insertComment(request))
                .build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/deleteComment/{commentId}")
    @Operation(summary = "delete comment")
    public ApiResponse<Void> deleteComment (@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ApiResponse.<Void>builder()
                .message("delete successfully")
                .build();
    }

    @PreAuthorize("isAuthenticated() or hasAuthority('ADMIN')")
    @PutMapping(value = "/updateComment/{commentId}")
    @Operation(summary = "update comment")
    public ApiResponse<CommentUpdateResponse> updateComment (@PathVariable Long commentId, @RequestBody @Valid CommentUpdateRequest request) {
        return ApiResponse.<CommentUpdateResponse>builder()
                .message("delete successfully")
                .result(commentService.updateComment(commentId,request))
                .build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/getUserComments/{userId}")
    @Operation(summary = "Comment by user")
    public ApiResponse<List<CommentResponse>> commentByUser (@PathVariable Long userId) {
        return ApiResponse.<List<CommentResponse>>builder()
                .message("successfully")
                .result(commentService.getUserComments(userId))
                .build();
    }
}
