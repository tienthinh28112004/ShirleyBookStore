package ApiWebManga.service.Impl;

import ApiWebManga.Entity.Notification;
import ApiWebManga.Entity.User;
import ApiWebManga.Entity.UserHasRoles;
import ApiWebManga.Enums.Role;
import ApiWebManga.Exception.BadCredentialException;
import ApiWebManga.Exception.NotFoundException;
import ApiWebManga.Utils.SecurityUtils;
import ApiWebManga.dto.Request.NotificationCreateRequest;
import ApiWebManga.dto.Response.NotificationResponse;
import ApiWebManga.repository.BookRepository;
import ApiWebManga.repository.NotificationRepository;
import ApiWebManga.repository.UserRepository;
import ApiWebManga.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationServiceImpl implements NotificationService {
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final BookRepository bookRepository;

    @PreAuthorize("isAuthenticated()")
    public List<NotificationResponse> getNotificationsForCurrentUser(){
        String email = SecurityUtils.getCurrentLogin()
                .orElseThrow(()->new BadCredentialException("Email không hợp lệ"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User not found"));

        List<Notification> notifications = notificationRepository.findByUserIdOrderByIdDesc(user.getId());
        if(notifications == null || notifications.isEmpty()){
            return Collections.emptyList();
        }
        return notifications.stream().map(NotificationResponse::convert).collect(Collectors.toList());
    }

    @Transactional
    @PreAuthorize("isAuthenticated()")
    public void createNotification(NotificationCreateRequest request){
        String email = SecurityUtils.getCurrentLogin()
                .orElseThrow(()->new NotFoundException("email not found"));

        User  userSender= userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User not found"));

        User userReceiver = userRepository.findById(request.getUserReceiverId())
                .orElseThrow(()->new NotFoundException("User not found"));
        boolean admin=false;
        for(UserHasRoles x:userSender.getUserHasRoles()){
            if(Objects.equals(x.getRole().getName(), Role.ADMIN)){
                admin=true;
            }
        }
        //if(admin){
            Notification notification = Notification.builder()
                    .user(userReceiver)//thông tin người nhận
                    .message(request.getMessage())
                    .title(request.getTitle())
                    .url(request.getUrl())
                    .isRead(false)//người nhận chưa đọc
                    .avatarUrl(userSender.getAvatarUrl())
                    .build();

            notificationRepository.save(notification);
//        }else{
//            throw new BadCredentialException("Bạn không có quyền");
//        }

    }

    @Transactional
    public void deleteNotification(Long notificationId){
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(()->new NotFoundException("notification not found"));

        notificationRepository.delete(notification);
    }

    public void markAsRead(Long notificationId){
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(()->new NotFoundException("notification not found"));

        notification.setIsRead(true);//đánh dấu đã đọc
        notificationRepository.save(notification);
    }


}
