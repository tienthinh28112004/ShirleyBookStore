package ApiWebManga.service;

import ApiWebManga.dto.Request.CommentRequest;
import ApiWebManga.dto.Request.CommentUpdateRequest;
import ApiWebManga.dto.Response.CommentResponse;
import ApiWebManga.dto.Response.CommentUpdateResponse;
import ApiWebManga.dto.Response.PageResponse;

import java.util.List;

public interface CommentService {
    CommentResponse insertComment(CommentRequest request);
    void deleteComment(Long commentId);
    CommentUpdateResponse updateComment(Long CommentId, CommentUpdateRequest request);
    PageResponse<List<CommentResponse>> getCommentsByBook(Long bookId, int page, int size);
    List<CommentResponse> getUserComments(Long userId);
}
