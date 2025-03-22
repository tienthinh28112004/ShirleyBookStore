package ApiWebManga.service;

import ApiWebManga.Entity.Notification;
import ApiWebManga.dto.Request.NotificationCreateRequest;
import ApiWebManga.dto.Response.BookDetailResponse;
import ApiWebManga.dto.Response.UserResponse;

import java.util.List;

public interface NotificationService {
    Notification insertNotification(NotificationCreateRequest request);
    List<Notification> getRecentNotifications();
    List<BookDetailResponse> getNotificationBookIds();
    List<UserResponse> getUsersFollowingBook(Long bookId);
}
