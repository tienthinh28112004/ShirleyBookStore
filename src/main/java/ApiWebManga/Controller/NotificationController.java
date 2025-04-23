package ApiWebManga.Controller;

import ApiWebManga.dto.Request.ApiResponse;
import ApiWebManga.dto.Request.NotificationCreateRequest;
import ApiWebManga.dto.Response.NotificationResponse;
import ApiWebManga.service.NotificationService;
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
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/getNotificationsForCurrentUser")//lấy tát cả thng báo của người đăng nhập
    public ApiResponse<List<NotificationResponse>> listNotification () {
        return ApiResponse.<List<NotificationResponse>>builder()
                .message("successfully")
                .result(notificationService.getNotificationsForCurrentUser())
                .build();
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/insertNotification")//tạo ra thông báo
    @Operation(summary = "create Notification")
    public ApiResponse<Void> insertNotification (@RequestBody @Valid NotificationCreateRequest request) {
        notificationService.createNotification(request);
        return ApiResponse.<Void>builder()
                .message("Tạo thông báo thành công")
                .build();
    }

    @PreAuthorize("isAuthenticated() or hasAuthority('ADMIN')")
    @DeleteMapping(value = "/deleteNotification/{notificationId}")//xóa thông báo
    public ApiResponse<Void> deleteNotification (@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ApiResponse.<Void>builder()
                .message("Xóa thông báo thành công")
                .build();
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping(value = "/markAsRead/{notificationId}")//đánh dẫu đã đọc
    public ApiResponse<Void> markAsRead (@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ApiResponse.<Void>builder()
                .message("successfully")
                .build();
    }
}
