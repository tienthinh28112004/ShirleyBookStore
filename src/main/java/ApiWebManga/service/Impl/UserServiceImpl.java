package ApiWebManga.service.Impl;

import ApiWebManga.Entity.*;
import ApiWebManga.Enums.RegistrationStatus;
import ApiWebManga.Exception.BadCredentialException;
import ApiWebManga.Exception.NotFoundException;
import ApiWebManga.Utils.SecurityUtils;
import ApiWebManga.dto.Request.NotificationCreateRequest;
import ApiWebManga.dto.Request.UserCreationRequest;
import ApiWebManga.dto.Request.UserUpdateRequest;
import ApiWebManga.dto.Response.PageResponse;
import ApiWebManga.dto.Response.TeacherApplicationDetailResponse;
import ApiWebManga.dto.Response.UserRegisterAuthorResponse;
import ApiWebManga.dto.Response.UserResponse;
import ApiWebManga.repository.RolesRepository;
import ApiWebManga.repository.SearchRepository;
import ApiWebManga.repository.UserRepository;
import ApiWebManga.service.EmailVerificationTokenService;
import ApiWebManga.service.NotificationService;
import ApiWebManga.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ApiWebManga.Enums.Role.AUTHOR;
import static ApiWebManga.Enums.Role.USER;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    public final  CloudinaryService cloudinaryService;
    private final RolesRepository rolesRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationTokenService emailVerificationTokenService;
    private final MailSenderService mailSenderService;
    private final NotificationService notificationService;
    private final SearchRepository searchRepository;

    @Override
    public PageResponse<List<UserResponse>> getAllUsersWithSortByMultipleColumns(int pageNo, int pageSize,String keyword, String sorts) {
        //sort
        List<Sort.Order> orders=new ArrayList<>();

        log.info("page:{},size{},keyword:{}",pageNo,pageSize,keyword);
        if(sorts!=null) {
            log.info("vào đến đấy?");
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sorts);
            if (matcher.find()) {
                if (matcher.group(3).equalsIgnoreCase("asc")) {
                    orders.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                } else {
                    orders.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                }
            }
        }

        //Paging
        Pageable pageable=PageRequest.of(pageNo-1,pageSize,Sort.by(orders));
        Page<User> userPage=null;
        if(StringUtils.hasLength(keyword)&&keyword!=null){
            userPage =userRepository.findAllByKeyword(pageable,keyword);
        }else{
            userPage = userRepository.findAll(pageable);
        }
        List<UserResponse> userList=userPage.stream().map(UserResponse::convert).toList();
        return PageResponse.<List<UserResponse>>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(userPage.getTotalPages())
                .items(userList)
                .totalElements(userList.size())
                .build();
    }

    @Override
    public User createUser(UserCreationRequest request) {
        log.info("vào đến đây rồi");

        User existingUser=userRepository.findByEmail(request.getEmail()).orElse(null);

        if(existingUser !=null){//nếu User đã tồn tại
            if(existingUser.getEmailVerifiedAt() != null){
                throw new BadCredentialsException("Email đã được đăng kí rồi");
            }else{
                try {
                    existingUser.setFullName(request.getFullName());
                    existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
                    //nếu user tồn tại nhwung chưa được xác thực,gwuir lại OTP
                    existingUser.setEmailVerificationToken(emailVerificationTokenService.create(existingUser));
                    mailSenderService.sendEmailUser(existingUser);
                    return existingUser;
                } catch (Exception e){
                    throw new BadCredentialsException("Gửi Email thất bại vui lòng thử lại");
                }
            }
        }

        Roles role =rolesRepository.findByName(String.valueOf(USER))
                .orElseThrow(()-> new NotFoundException("role nay khong ton tai@@@"));


        User user=User.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .password(passwordEncoder.encode(request.getPassword()))
                .isActive(false)//chỉ kích hoạt khi user xác thực email
                .avatarUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSp3ztVtyMtzjiT_yIthons_zqTQ_TNZm4PS0LxFyFO0ozfM2S87W8QoL4&s")
                //avatar mặc định như facebook
                .build();
        userRepository.save(user);
        UserHasRoles userHasRole = UserHasRoles.builder()
                .role(role)
                .user(user)
                .build();
        Set<UserHasRoles> listUserHashRole =new HashSet<>();
        listUserHashRole.add(userHasRole);
        List<CartDetail> cartDetailList= new ArrayList<>();
        Cart cart = Cart.builder()
                .user(user)
                .totalMoney(0L)
                .cartDetails(cartDetailList)
                .build();

        user.setCart(cart);

        user.setUserHasRoles(listUserHashRole);
        try {
            user.setEmailVerificationToken(emailVerificationTokenService.create(user));
            mailSenderService.sendEmailUser(user);
            log.info("User được lưu thành công");
        } catch (Exception e){
            throw new BadCredentialsException("Gửi Email thất bại vui lòng thử lại");
        }
        return user;
    }

    @Override
    public User findById(Long id) {
        //xử lí lỗi
        return userRepository.findById(id).orElseThrow(()->new NotFoundException("User not found"));
    }

    @Override
    public User update(UserUpdateRequest request) {
        String email=SecurityUtils.getCurrentLogin()
                .orElseThrow(()->new BadCredentialException("email invalid"));
        User user=userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User not found"));

        //boolean isRequiredEmailVerification = false;
        if (StringUtils.hasText(request.getFullName()) && !request.getFullName().equals(user.getFullName())) {
            user.setFullName(request.getFullName());
        }

        if (request.getDob() != null && !Objects.equals(request.getDob(), user.getBirthday())) {
            user.setBirthday(request.getDob());
        }

        if (StringUtils.hasText(request.getPhoneNumber()) && !request.getPhoneNumber().equals(user.getPhoneNumber())) {
            user.setPhoneNumber(request.getPhoneNumber());
        }

// Sửa lỗi: lẽ ra setAvatarUrl chứ không phải setPhoneNumber
        if (StringUtils.hasText(request.getAvatar()) && !request.getAvatar().equals(user.getAvatarUrl())) {
            user.setAvatarUrl(request.getAvatar());
        }


        // user.setRoles(request.getRoles());
//        if (isRequiredEmailVerification) {
//            emailVerificationEventPublisher(user);
//        }
//        if (request.getRoles() != null) {
//            user.setRoles(request.getRoles().stream()
//                    .map(role -> roleService.findByName(Constants.RoleEnum.get(role)))
//                    .collect(Collectors.toList()));
//        }
        userRepository.save(user);
        return user;
    }

    @Override
    public void banUser(Long userId) {
        User user=userRepository.findById(userId)
                .orElseThrow(()->new NotFoundException("User Not found"));
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public void unBanUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new NotFoundException("User not found"));
        user.setActive(true);
        userRepository.save(user);
    }


    @Override
    public User getMyInfo() {
        String email = SecurityUtils.getCurrentLogin()
                .orElseThrow(()->new BadCredentialsException("unauthorized"));
        log.info("email {}",email);
        User user=userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User not found"));
        return user;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') and isAuthenticated()")
    public UserRegisterAuthorResponse updateRoleAuthor(long userId) {
        User user=userRepository.findById(userId)
                .orElseThrow(()->new NotFoundException("User not found"));
        Roles roles= rolesRepository.findByName(AUTHOR.name())
                .orElseThrow(()->new  BadCredentialException("Không tồn tại role này"));

        boolean author=false;
        for (UserHasRoles userRole : user.getUserHasRoles()) {
            if (userRole.getRole().getName().equals(AUTHOR.name())) {
                author = true;
                break;
            }
        }
        log.info("{}",author);
        if(RegistrationStatus.PENDING.equals(user.getRegistrationStatus())||RegistrationStatus.REJECT.equals(user.getRegistrationStatus())&&!author) {
            log.info("1");
            UserHasRoles userHasRoles=UserHasRoles.builder()
                    .user(user)
                    .role(roles)
                    .build();
            user.setRegistrationStatus(RegistrationStatus.APPROVE);
            user.getUserHasRoles().add(userHasRoles);
            userRepository.save(user);
            NotificationCreateRequest request = NotificationCreateRequest.builder()
                    .userReceiverId(userId)
                    .message("Your application to become a author has been approved.")
                    .title("Teacher Registration Approved")
                    .url("/author")
                    .build();
            notificationService.createNotification(request);
        }
        return UserRegisterAuthorResponse.convert(user);
    }
    @Override
    @PreAuthorize("hasAuthority('ADMIN') and isAuthenticated()")
    public UserRegisterAuthorResponse banAuthor(long userId) {
        User user=userRepository.findById(userId)
                .orElseThrow(()->new NotFoundException("User not found"));
        Roles roles= rolesRepository.findByName(AUTHOR.name())
                .orElseThrow(()->new  BadCredentialException("Không tồn tại role này"));

        UserHasRoles author=null;
        for (UserHasRoles userRole : user.getUserHasRoles()) {
            if (userRole.getRole().getName().equals(AUTHOR.name())) {
                author = userRole;
                break;
            }
        }
        log.info("{}",author);
        if(RegistrationStatus.PENDING.equals(user.getRegistrationStatus())||RegistrationStatus.APPROVE.equals(user.getRegistrationStatus())&&author!=null) {
            user.setRegistrationStatus(RegistrationStatus.REJECT);
            user.setBio(null);
            user.setCertificate(null);
            user.setCvUrl(null);
            user.setFacebookLink(null);
            user.getUserHasRoles().remove(author);
            userRepository.save(user);

            NotificationCreateRequest request=NotificationCreateRequest.builder()
                    .userReceiverId(userId)
                    .message("Your application to become a teacher has been rejected.")
                    .title("Teacher Registration Rejected")
                    .url("/support")
                    .build();
            notificationService.createNotification(request);
        }
        return UserRegisterAuthorResponse.convert(user);
    }
    @Transactional(readOnly = true)
    public TeacherApplicationDetailResponse getUserApplicationDetail(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new NotFoundException("User not found"));

        return TeacherApplicationDetailResponse.convert(user);
    }
}
