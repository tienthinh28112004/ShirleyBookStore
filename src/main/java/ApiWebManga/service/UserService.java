package ApiWebManga.service;

import ApiWebManga.Entity.User;
import ApiWebManga.dto.Request.UserCreationRequest;
import ApiWebManga.dto.Request.UserUpdateRequest;
import ApiWebManga.dto.Response.PageResponse;
import ApiWebManga.dto.Response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    PageResponse<List<UserResponse>> getAllUsersWithSortByMultipleColumns(int pageNo, int pageSize, String keyword, String sorts);
    User createUser(UserCreationRequest request);
    User findById(Long id);
    User update(Long userId, UserUpdateRequest request, MultipartFile avatarPdf);
    void banUser(Long userId);
    void unBanUser(Long userId);
    User getMyInfo();
    void updateRoleAuthor(long userId);

}
