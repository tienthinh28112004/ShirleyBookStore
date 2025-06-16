package ApiWebManga.Controller;

import ApiWebManga.dto.Request.ApiResponse;
import ApiWebManga.dto.Request.UserCreationRequest;
import ApiWebManga.dto.Request.UserUpdateRequest;
import ApiWebManga.dto.Response.PageResponse;
import ApiWebManga.dto.Response.TeacherApplicationDetailResponse;
import ApiWebManga.dto.Response.UserRegisterAuthorResponse;
import ApiWebManga.dto.Response.UserResponse;
import ApiWebManga.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Get list of users with sort by multiple columns", description = "Send a request via this API to get user list by pageNo, pageSize and sort by multiple column")
    @GetMapping("/list-with-sort-by-multiple-columns")
    public ApiResponse<PageResponse<List<UserResponse>>> getAllUsersWithSortByMultipleColumns(
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
           @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sorts) {
        log.info("Request get all of users with sort by multiple columns");
        return ApiResponse.<PageResponse<List<UserResponse>>>builder()
                .message("list users")
                .result(userService.getAllUsersWithSortByMultipleColumns(page, size,keyword, sorts))
                .build();
    }

    @PostMapping("/addUser")//để chờ register gọi đến
    @Operation(summary = "Create user endpoint")
    public ApiResponse<UserResponse> createUser(
            @Parameter(description = "Request body to user create", required = true)
            @RequestBody @Valid UserCreationRequest request){
        log.info("add user");
        return ApiResponse.<UserResponse>builder()
                .message("create sucessfully")
                .result(UserResponse.convert(userService.createUser(request)))
                .build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{userId}")
    @Operation(summary = "Show user endpoint")
    public ApiResponse<UserResponse> getUser(
            @Parameter(name = "id", description = "User ID", required = true)
            @PathVariable("userId") final Long userId) {
        return ApiResponse.<UserResponse>builder()
                .message("show user")
                .result(UserResponse.convert(userService.findById(userId)))
                .build();
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/update")
    @Operation(summary = "Update user endpoint")
    public ApiResponse<UserResponse> updateUser(
            @Valid @RequestBody UserUpdateRequest request
    ) {
        return ApiResponse.<UserResponse>builder()
                .message("update User")
                .result(UserResponse.convert(userService.update(request)))
                .build();
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/banUser/{userId}")
    @Operation(summary = "Delete user endpoint")
    public ApiResponse<String> banUser(
            @Parameter(name = "id", description = "User ID", required = true)
            @PathVariable("userId") final Long userId){
        userService.banUser(userId);
        return ApiResponse.<String>builder().
                result("User has been deleted").
                build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/unBanUser/{userId}")
    @Operation(summary = "Delete user endpoint")
    public ApiResponse<String> unBanUser(
            @Parameter(name = "id", description = "User ID", required = true)
            @PathVariable("userId") final Long userId){
        userService.unBanUser(userId);
        return ApiResponse.<String>builder().
                result("User has been deleted").
                build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping (value = "/myInfo")
    @Operation(summary = "user authenticated")
    public ApiResponse<UserResponse> getMyInfo () {
        return ApiResponse.<UserResponse>builder()
                .result(UserResponse.convert(userService.getMyInfo()))
                .build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/updateRoleAuthor/{userId}")
    @Operation(summary = "Upload Role")
    public ApiResponse<UserRegisterAuthorResponse> updateRoleAuthor(
            @Parameter(name = "id", description = "User ID", required = true)
            @PathVariable("userId") Long userId){
        return ApiResponse.<UserRegisterAuthorResponse>builder()
                .message("Update successfully")
                .result(userService.updateRoleAuthor(userId))
                .build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/banAuthor/{userId}")
    @Operation(summary = "Upload Role")
    public ApiResponse<UserRegisterAuthorResponse> banAuthor(
            @Parameter(name = "id", description = "User ID", required = true)
            @PathVariable("userId") Long userId){
        userService.banAuthor(userId);
        return ApiResponse.<UserRegisterAuthorResponse>builder().
                message("Update successfully").
                build();
    }
    @GetMapping("/{userId}/details")
    public ApiResponse<TeacherApplicationDetailResponse> getUserApplicationDetail(@PathVariable Long userId) {
        TeacherApplicationDetailResponse details = userService.getUserApplicationDetail(userId);
        return ApiResponse.<TeacherApplicationDetailResponse>builder().
                message("Update successfully").
                result(details).
                build();
    }
}
