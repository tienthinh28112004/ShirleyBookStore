package ApiWebManga.service;

import ApiWebManga.dto.Request.UserRegisterAuthorRequest;
import ApiWebManga.dto.Response.UserRegisterAuthorResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RegisterAuthorService {
    List<UserRegisterAuthorResponse> getAll();
    UserRegisterAuthorResponse registerAuthor(UserRegisterAuthorRequest request,MultipartFile cv,
                                              MultipartFile certificate);

}
