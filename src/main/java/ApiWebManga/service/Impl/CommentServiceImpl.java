package ApiWebManga.service.Impl;

import ApiWebManga.Entity.Book;
import ApiWebManga.Entity.Comment;
import ApiWebManga.Entity.User;
import ApiWebManga.Entity.UserHasRoles;
import ApiWebManga.Enums.Role;
import ApiWebManga.Exception.BadCredentialException;
import ApiWebManga.Exception.NotFoundException;
import ApiWebManga.Utils.SecurityUtils;
import ApiWebManga.dto.Request.CommentRequest;
import ApiWebManga.dto.Request.CommentUpdateRequest;
import ApiWebManga.dto.Response.CommentResponse;
import ApiWebManga.dto.Response.CommentUpdateResponse;
import ApiWebManga.dto.Response.PageResponse;
import ApiWebManga.repository.BookRepository;
import ApiWebManga.repository.CommentRepository;
import ApiWebManga.repository.UserRepository;
import ApiWebManga.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;

    @Override//lấy comment trong sách
    public PageResponse<List<CommentResponse>> getCommentsByBook(Long bookId, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"createdAt");//sắp xếp giảm theo ngày tạo
        Pageable pageable = PageRequest.of(page -1 ,size,sort);

        //lấy ra các comment của sách và có parentComment là null(tức là comment ấy chính là comment đầu tiên)
        Page<Comment> parentComments = commentRepository.findCommentByBookIdAndParentCommentIsNull(bookId,pageable);

        List<Comment> totalComment = commentRepository.findCommentByBookId( bookId );
        List<CommentResponse> responses = parentComments.stream().map(CommentResponse::convert).toList();

        return PageResponse.<List<CommentResponse>>builder()
                .pageNo(page)//page hiện tại
                .pageSize(pageable.getPageSize())//số lương page
                .totalElements(totalComment.size())//chỉ cần là page đều lấy được getTotalElement
                .totalPages(responses.size())
                .items(responses)
                .build();
    }
    @Override
    @Transactional//tạo ra comment
    @PreAuthorize("isAuthenticated()")
    public CommentResponse insertComment(CommentRequest request) {
        String email = SecurityUtils.getCurrentLogin()
                .orElseThrow(()->new NotFoundException("Email invalid"));

        User user = userRepository.findByEmail(email)
               .orElseThrow(()->new NotFoundException("User not found"));

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(()->new NotFoundException("Book not found"));

        Comment parentComment = null;
        if(request.getParentCommentId() != null){//nếu là null thì đây là comment gốc
            parentComment = commentRepository.findById(request.getParentCommentId())//tìm cha của comment
                    .orElseThrow(()->new NotFoundException("Parent comment not existed"));
        }

        if(!StringUtils.hasLength(request.getContent())){
            //nếu nó rỗng hoặc null thì đưa ra lỗi
            throw new BadCredentialException("Invalid comment content");
        }

        Comment newComment = Comment.builder()
                .parentComment(parentComment)
                .content(request.getContent())
                .user(user)
                .book(book)
                .build();

        commentRepository.save(newComment);
        return CommentResponse.convert(newComment);
    }

    @Override
    @Transactional
    @PreAuthorize("isAuthenticated")
    public void deleteComment(Long commentId) {
        //chỉ có người tạo hoặc admin mới được xóa comment
        String email = SecurityUtils.getCurrentLogin()
                .orElseThrow(()->new NotFoundException("User not found"));

        User user= userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("Comment not exist"));

        Comment comment=commentRepository.findById(commentId)
                .orElseThrow(()->new NotFoundException("Comment not found"));
        //check xem người dùng có quyền admin hay không
        boolean admin=false;
        for(UserHasRoles x: user.getUserHasRoles()){
            if(Objects.equals(x.getRole().getName(),Role.ADMIN)){
                admin=true;
            }
        }
        if(admin || Objects.equals(user.getId(),comment.getUser().getId())){
            commentRepository.deleteById(commentId);
        }
    }

    @Override//update comment
    public CommentUpdateResponse updateComment(Long commentId, CommentUpdateRequest request) {
       String email = SecurityUtils.getCurrentLogin()
               .orElseThrow(()->new NotFoundException("User not found"));

       User user= userRepository.findByEmail(email)
               .orElseThrow(()->new NotFoundException("Comment not exist"));

        Comment comment=commentRepository.findById(commentId)
                .orElseThrow(()->new NotFoundException("Comment not found"));

        if(!Objects.equals(user.getId(),comment.getUser().getId())){
            throw new BadCredentialException("Update comment invalid");
        }

        comment.setContent(request.getContent());
        commentRepository.save(comment);
       return CommentUpdateResponse.builder()
               .id(comment.getId())
               .content(comment.getContent())
               .build();
    }

    @Override//lất comment cuủa user
    public List<CommentResponse> getUserComments(Long userId) {
        List<Comment> comments=commentRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return comments.stream()
                .map(CommentResponse::convert)
                .collect(Collectors.toList());
    }


}
