package ApiWebManga.service.Impl;

import ApiWebManga.Entity.Book;
import ApiWebManga.Entity.Comment;
import ApiWebManga.Entity.User;
import ApiWebManga.Exception.NotFoundException;
import ApiWebManga.dto.Request.CommentRequest;
import ApiWebManga.dto.Request.CommentUpdateRequest;
import ApiWebManga.dto.Response.CommentResponse;
import ApiWebManga.repository.BookRepository;
import ApiWebManga.repository.CommentRepository;
import ApiWebManga.repository.UserRepository;
import ApiWebManga.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional//tạo ra comment
    public CommentResponse insertComment(CommentRequest request) {
        User user = userRepository.findById(request.getUserId())//đáng nhẽ phải so sánh với cả getCurrentLogin nữa những chắc fontend sẽ lấy được thông tin tài khaonr đăng nhập nên khng cần
                .orElseThrow(()->new NotFoundException("User not found"));

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(()->new NotFoundException("Bool not found"));

        Comment newComment = Comment.builder()
                .user(user)
                .book(book)
                .content(request.getContent())
                .createAt(LocalDateTime.now())
                .build();
        commentRepository.save(newComment);
        return CommentResponse.convert(newComment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override//update comment
    public CommentResponse updateComment(Long commentId, CommentUpdateRequest request) {
        Comment comment=commentRepository.findById(commentId)
                .orElseThrow(()->new NotFoundException("Comment not found"));
        comment.setContent(request.getContent());
        comment.setCreateAt(LocalDateTime.now());
        commentRepository.save(comment);
        return CommentResponse.convert(comment);
    }

    @Override//lấy comment của user trong sách
    public List<CommentResponse> getCommentsByUserAndBook(Long userId, Long bookId) {
        List<Comment> comments = commentRepository.findByUserIdAndBookId(userId,bookId);
        return comments.stream()
                .map(CommentResponse::convert)
                .collect(Collectors.toList());
    }

    @Override//lấy comment trong sách
    public List<CommentResponse> getCommentsByBook(Long bookId) {
        List<Comment> comments = commentRepository.findByBookId(bookId);
        return comments.stream()
                .map(CommentResponse::convert)
                .collect(Collectors.toList());
    }

    @Override//lất comment cuủa user
    public List<CommentResponse> getUserComments(Long userId) {
        List<Comment> comments=commentRepository.findByUserIdOrderByCreateAtDesc(userId);
        return comments.stream()
                .map(CommentResponse::convert)
                .collect(Collectors.toList());
    }


}
