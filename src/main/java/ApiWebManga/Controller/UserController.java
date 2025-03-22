package ApiWebManga.Controller;

import ApiWebManga.Entity.User;
import ApiWebManga.dto.Request.ApiResponse;
import ApiWebManga.dto.Request.UserCreationRequest;
import ApiWebManga.dto.Request.UserUpdateRequest;
import ApiWebManga.dto.Response.PageResponse;
import ApiWebManga.dto.Response.UserResponse;
import ApiWebManga.service.EmailVerificationTokenService;
import ApiWebManga.service.Impl.MailSenderService;
import ApiWebManga.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
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

    @PostAuthorize("ADMIN")
    @Operation(summary = "Get list of users per pageNo", description = "Send a request via this API to get user list by pageNo and pageSize")
    @GetMapping("/list")
    public ApiResponse<?> getAllUsers(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                       @RequestParam(defaultValue = "20", required = false) int pageSize,
                                       @RequestParam(required = false) String sortBy) {
        log.info("Request get all of users");
        return ApiResponse.<PageResponse>builder()
                .message("list users")
                .result(userService.getAllUsersWithSortBy(pageNo, pageSize, sortBy))
                .build();
    }
    @PostAuthorize("ADMIN")
    @Operation(summary = "Get list of users with sort by multiple columns", description = "Send a request via this API to get user list by pageNo, pageSize and sort by multiple column")
    @GetMapping("/list-with-sort-by-multiple-columns")
    public ApiResponse<?> getAllUsersWithSortByMultipleColumns(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                                @RequestParam(defaultValue = "20", required = false) int pageSize,
                                                                @RequestParam(required = false) String... sorts) {
        log.info("Request get all of users with sort by multiple columns");
        return ApiResponse.<PageResponse>builder()
                .message("list users")
                .result(userService.getAllUsersWithSortByMultipleColumns(pageNo, pageSize, sorts))
                .build();
    }

    @PostAuthorize("ADMIN")
    @Operation(summary = "Advance search query by criteria", description = "Send a request via this API to get user list by pageNo, pageSize and sort by multiple column")
    @GetMapping("/advance-search-with-criteria")//chưa làm
    public ApiResponse<?> advanceSearchWithCriteria(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                     @RequestParam(defaultValue = "20", required = false) int pageSize,
                                                     @RequestParam(required = false) String sortBy,
                                                     @RequestParam(defaultValue = "") String... search) {
        log.info("Request advance search query by criteria");
        return ApiResponse.<PageResponse>builder()
                .message("list users")
                .result(userService.advanceSearchWithCriteria(pageNo, pageSize, sortBy, search))
                .build();
    }

    @PostAuthorize("ADMIN")
    @PostMapping("/add")
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

    //@PostAuthorize("#email == authentication.token.claims['sub'] or hasRole('ADMIN')")
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

    @PostAuthorize("#email == authentication.token.claims['sub'] or hasRole('ADMIN')")
    @PatchMapping("/update/{userId}")
    @Operation(summary = "Update user endpoint")
    public ApiResponse<UserResponse> updateUser(
            @Parameter(description = "userID", required = true)
            @PathVariable("userId") Long userId,
            @Parameter(description = "Request body to user update", required = true)
            @RequestBody @Valid UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .message("update User")
                .result(UserResponse.convert(userService.update(userId,request)))
                .build();

    }

    @PostAuthorize("#email == authentication.token.claims['sub'] or hasRole('ADMIN')")
    @DeleteMapping("/delete/{userId}")
    @Operation(summary = "Delete user endpoint")
    public ApiResponse<String> deleteUser(
            @Parameter(name = "id", description = "User ID", required = true)
            @PathVariable("userId") final Long userId){
        userService.delete(userId);
        return ApiResponse.<String>builder().
                result("User has been deleted").
                build();
    }

    @PostAuthorize("#email == authentication.token.claims['sub'] or hasRole('ADMIN')")
    @GetMapping (value = "/myInfo")
    @Operation(summary = "user authencated")
    public ApiResponse<UserResponse> getMyInfo () {
        return ApiResponse.<UserResponse>builder()
                .result(UserResponse.convert(userService.getMyInfo()))
                .build();
    }
}
