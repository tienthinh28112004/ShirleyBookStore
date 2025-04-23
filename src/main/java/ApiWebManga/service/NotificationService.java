package ApiWebManga.service;

import ApiWebManga.dto.Request.NotificationCreateRequest;
import ApiWebManga.dto.Response.NotificationResponse;

import java.util.List;

public interface NotificationService {
    List<NotificationResponse> getNotificationsForCurrentUser();
    void createNotification(NotificationCreateRequest request);
    void deleteNotification(Long notificationId);
    void markAsRead(Long notificationId);
}
