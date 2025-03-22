package ApiWebManga.service;

import ApiWebManga.Entity.User;
import ApiWebManga.dto.Request.UserCreationRequest;
import ApiWebManga.dto.Request.UserUpdateRequest;
import ApiWebManga.dto.Response.PageResponse;

public interface UserService {

    PageResponse<?> advanceSearchWithCriteria(int pageNo, int pageSize, String sortBy, String... search);
    PageResponse<?> getAllUsersWithSortByMultipleColumns(int pageNo, int pageSize, String... sorts);
    PageResponse<?> getAllUsersWithSortBy(int pageNo,int pageSize,String sortBy);
    User createUser(UserCreationRequest request);
    User findById(Long id);
    User update(Long userId,UserUpdateRequest request);
    void delete(Long userId);
    User getMyInfo();

}
