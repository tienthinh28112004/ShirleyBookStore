package ApiWebManga.Controller;

import ApiWebManga.dto.Request.ApiResponse;
import ApiWebManga.dto.Request.UserRegisterAuthorRequest;
import ApiWebManga.dto.Response.UserRegisterAuthorResponse;
import ApiWebManga.service.RegisterAuthorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/registerAuthor")
@Slf4j
public class RegisterAuthorController {
    private final RegisterAuthorService registerAuthorService;

    @GetMapping("/registration-authors")
    ApiResponse<List<UserRegisterAuthorResponse>> getAll(){
        log.info("{}",registerAuthorService.getAll());
        return ApiResponse.<List<UserRegisterAuthorResponse>>builder()
                .result(registerAuthorService.getAll())
                .build();
    }

    @PostMapping("/register-author")
    ApiResponse<UserRegisterAuthorResponse> registerAuthor(
            @RequestPart("request") UserRegisterAuthorRequest request,
            @RequestPart("cv") MultipartFile cv,
            @RequestPart("certificate") MultipartFile certificate
    ) throws IOException, URISyntaxException {

        var result = registerAuthorService.registerAuthor(request, cv, certificate);

        return ApiResponse.<UserRegisterAuthorResponse>builder()
                .result(result)
                .build();
    }

}
