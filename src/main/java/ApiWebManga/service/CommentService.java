package ApiWebManga.service;

import ApiWebManga.dto.Request.CommentRequest;
import ApiWebManga.dto.Request.CommentUpdateRequest;
import ApiWebManga.dto.Response.CommentResponse;

import java.util.List;

public interface CommentService {
    CommentResponse insertComment(CommentRequest request);
    void deleteComment(Long commentId);
    CommentResponse updateComment(Long CommentId, CommentUpdateRequest request);
    List<CommentResponse> getCommentsByUserAndBook(Long userId,Long bookId);
    List<CommentResponse> getCommentsByBook(Long bookId);
    List<CommentResponse> getUserComments(Long userId);
    //insert,delete,update,
    // getCommentsByUserAndBook
    //getCommentsByBook
    //getUserComments
}
