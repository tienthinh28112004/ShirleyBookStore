package ApiWebManga.service.Impl;

import ApiWebManga.Entity.User;
import ApiWebManga.Enums.RegistrationStatus;
import ApiWebManga.Exception.NotFoundException;
import ApiWebManga.Utils.SecurityUtils;
import ApiWebManga.dto.Request.NotificationCreateRequest;
import ApiWebManga.dto.Request.UserRegisterAuthorRequest;
import ApiWebManga.dto.Response.UserRegisterAuthorResponse;
import ApiWebManga.repository.UserRepository;
import ApiWebManga.service.NotificationService;
import ApiWebManga.service.RegisterAuthorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterAuthorServiceImpl implements RegisterAuthorService {
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final CloudinaryService cloudinaryService;

    public List<UserRegisterAuthorResponse> getAll() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        log.info("Lấy được danh sách các tác gải thành công");
        return userRepository.findAll(sort)
                .stream()
                .filter(user ->
                        user != null &&
                                user.getRegistrationStatus() != null &&  // Add null check
                                RegistrationStatus.PENDING.equals(user.getRegistrationStatus()) &&
                                user.getUserHasRoles() != null &&
                                !user.getUserHasRoles().isEmpty())
                .map(UserRegisterAuthorResponse::convert)
                .collect(Collectors.toList());
    }


    @Transactional
    @PreAuthorize("hasAuthority('USER') and isAuthenticated()")
    public UserRegisterAuthorResponse registerAuthor(UserRegisterAuthorRequest request,
                                                     MultipartFile cv,
                                                     MultipartFile certificate){
        String email= SecurityUtils.getCurrentLogin()
                .orElseThrow(()->new BadCredentialsException("email invalid"));
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User not found"));

        if(user.getRegistrationStatus() == null || user.getRegistrationStatus().equals(RegistrationStatus.REJECT)){
            String cvUrl=null;
            if(cv!=null){
                cvUrl = cloudinaryService.uploadImage(cv);
            }
            String certificateUrl=null;
            if(certificate!=null){
                certificateUrl= cloudinaryService.uploadImage(certificate);
            }
            user.setBio(request.getBio());
            user.setFacebookLink(request.getFacebookLink());
            user.setCvUrl(cvUrl);
            user.setCertificate(certificateUrl);
            user.setEmail(request.getEmail());
            user.setPhoneNumber(request.getPhone());
            user.setExpertise(request.getExpertise());
            user.setYearsOfExperience(request.getYearsOfExperience());
            user.setFullName(request.getName());
            user.setRegistrationStatus(RegistrationStatus.PENDING);

            userRepository.save(user);

            NotificationCreateRequest createRequest=NotificationCreateRequest.builder()
                    .userReceiverId(user.getId())
                    .message("A new teacher application has been submitted.")
                    .title("New Teacher Registration")
                    .url("/admin/author-application")
                    .build();
            notificationService.createNotification(createRequest);
        }
        return UserRegisterAuthorResponse.convert(user);
    }
}
