package ApiWebManga.Controller;

import ApiWebManga.Entity.Notification;
import ApiWebManga.dto.Request.ApiResponse;
import ApiWebManga.dto.Request.NotificationCreateRequest;
import ApiWebManga.dto.Response.BookDetailResponse;
import ApiWebManga.dto.Response.UserResponse;
import ApiWebManga.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/notification")
public class NotificationController {
//    Notification insertNotification(NotificationCreateRequest request);
//    boolean toggleNotification(Long bookId);
//    List<Notification> getRecentNotifications();
//    List<BookDetailResponse> getNotificationBookIds();
//    List<UserResponse> getUsersFollowingBook(Long bookId);
    private final NotificationService notificationService;
    @PostAuthorize("ADMIN")
    @PostMapping(value = "/insertNotification")
    @Operation(summary = "create Notification")
    public ApiResponse<Notification> insertNotification (@RequestBody @Valid NotificationCreateRequest request) {
        return ApiResponse.<Notification>builder()
                .message("insert successfully")
                .result(notificationService.insertNotification(request))
                .build();
    }
//    @PostMapping(value = "/toggleNotification/{bookId}")//thay đổi trạng thái thông báo(bật tắt thông báo)
//    @Operation(summary = "change notification")
//    public ApiResponse<Boolean> toggleNotification (@PathVariable Long bookId) {
//        return ApiResponse.<Boolean>builder()
//                .message("change successfully")
//                .result(notificationService.toggleNotification(bookId))
//                .build();
//    }

    @GetMapping(value = "/notificationBook")
    @Operation(summary = "information book")
    public ApiResponse<List<BookDetailResponse>> informationBook () {
        return ApiResponse.<List<BookDetailResponse>>builder()
                .message("successfully")
                .result(notificationService.getNotificationBookIds())
                .build();
    }
    @GetMapping(value = "/recentNotifications")
    @Operation(summary = "get Recent notification")
    public ApiResponse<List<Notification>> recentNotification () {
        return ApiResponse.<List<Notification>>builder()
                .message("successfully")
                .result(notificationService.getRecentNotifications())
                .build();
    }
    @GetMapping(value = "/getUsersFollowingBook/{bookId}")
    @Operation(summary = "information user follow book")
    public ApiResponse<List<UserResponse>> listUserFollowBook (@PathVariable Long bookId) {
        return ApiResponse.<List<UserResponse>>builder()
                .message("delete successfully")
                .result(notificationService.getUsersFollowingBook(bookId))
                .build();
    }
}
