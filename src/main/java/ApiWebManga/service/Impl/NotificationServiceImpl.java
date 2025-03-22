package ApiWebManga.service.Impl;

import ApiWebManga.Entity.Book;
import ApiWebManga.Entity.Notification;
import ApiWebManga.Entity.User;
import ApiWebManga.Exception.NotFoundException;
import ApiWebManga.Utils.SecurityUtils;
import ApiWebManga.dto.Request.NotificationCreateRequest;
import ApiWebManga.dto.Response.BookDetailResponse;
import ApiWebManga.dto.Response.UserResponse;
import ApiWebManga.repository.BookRepository;
import ApiWebManga.repository.NotificationRepository;
import ApiWebManga.repository.UserRepository;
import ApiWebManga.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationServiceImpl implements NotificationService {
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final BookRepository bookRepository;
    @Override
    public Notification insertNotification(NotificationCreateRequest request) {
        Long userId = getUserIdCurrent();
        User user = userRepository.findById(userId)
                .orElseThrow(()->new NotFoundException("User not found"));

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new NotFoundException("Book not found"));

        //cẩn thận trường hợp book không có chapter như yêu cầu
        if(request.getChapterId()==null || request.getChapterId()>book.getChapters().size()){
            throw new NotFoundException("Chapter not found");
        }
        Notification existingNotification = notificationRepository.findByUserIdAndBookId(userId, request.getBookId());
        if (existingNotification != null) {
            // Nếu đã tồn tại, cập nhật thông báo
            existingNotification.setEnabled(true);
            existingNotification.setMessage(request.getMessage());
            return notificationRepository.save(existingNotification);
        }
        return notificationRepository.save(
                Notification.builder()
                        .bookId(request.getBookId())
                        .chapterId(request.getChapterId())
                        .userId(userId)
                        .message(request.getMessage())
                        .isEnabled(true)//người dùng chưa tắt thông báo về quyển sách này
                        .build()
        );//2 trường createBy và updateBy chưa đưa vào được vì nó không kết thừa nhưng sẽ xử lí sau


    }

    @Override
    public List<Notification> getRecentNotifications() {
        Long userId =getUserIdCurrent();
        return notificationRepository.findRecentByUserId(userId, PageRequest.of(0,10));
    }

    @Override//lấy ra dánh sánh quyển sách mà user theo doi
    public List<BookDetailResponse> getNotificationBookIds() {
        Long userId =getUserIdCurrent();
        List<Long> bookIds=notificationRepository.findBookIdsByUserId(userId);
        List<Book> listBook= bookRepository.findAllById(bookIds);
        return listBook.stream().map(BookDetailResponse::convert).collect(Collectors.toList());
    }

    @Override//lấy ra danh sách người dùng thoe dõi quyển sách anyf
    public List<UserResponse> getUsersFollowingBook(Long bookId) {
        List<Long> userIds= notificationRepository.findUserIdsByBookId(bookId);
        List<User> listUser=userRepository.findAllById(userIds);
        return listUser.stream().map(UserResponse::convert).collect(Collectors.toList());
    }

    public Long getUserIdCurrent(){
        String email = SecurityUtils.getCurrentLogin()
                .orElseThrow(()->new NotFoundException("User chưa đăng nhập"));
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User not found"));
        return user.getId();
    }
}
