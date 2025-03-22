package ApiWebManga.service.Impl;

import ApiWebManga.Entity.*;
import ApiWebManga.Enums.Role;
import ApiWebManga.Exception.NotFoundException;
import ApiWebManga.Utils.SecurityUtils;
import ApiWebManga.dto.Request.UserCreationRequest;
import ApiWebManga.dto.Request.UserUpdateRequest;
import ApiWebManga.dto.Response.PageResponse;
import ApiWebManga.dto.Response.UserResponse;
import ApiWebManga.repository.RolesRepository;
import ApiWebManga.repository.UserRepository;
import ApiWebManga.service.EmailVerificationTokenService;
import ApiWebManga.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final RolesRepository rolesRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationTokenService emailVerificationTokenService;
    private final MailSenderService mailSenderService;
    @Override
    public PageResponse<?> advanceSearchWithCriteria(int pageNo, int pageSize, String sortBy, String... search) {
        return null;
    }

    @Override
    public PageResponse<?> getAllUsersWithSortByMultipleColumns(int pageNo, int pageSize, String... sorts) {
        int page=0;
        if(pageNo>0){
            page=pageNo-1;
        }
        //sort
        List<Sort.Order> orders=new ArrayList<>();
        if(sorts.length>0){
            for(String sort : sorts){
                Pattern pattern=Pattern.compile("(\\w+?)(:)(.*)");
                Matcher matcher=pattern.matcher(sort);
                if(matcher.find()){
                    if(matcher.group(3).equalsIgnoreCase("asc")){
                        orders.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                    }else{
                        orders.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                    }
                }
            }
        }
        //Paging
        Pageable pageable=PageRequest.of(page,pageSize,Sort.by(orders));

        Page<User> userPage =userRepository.findAllActiveUsers(pageable);

        List<UserResponse> userList=userPage.stream().map(UserResponse::convert).toList();
        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(userPage.getTotalPages())
                .items(userList)
                .build();
    }

    @Override
    public PageResponse<?> getAllUsersWithSortBy(int pageNo,int pageSize,String sortBy) {
        //Xu ly truong hop page muon bat dau tai 1
        int page =0;
        if(pageNo > 0){
            page=pageNo-1;
        }
        //Sorting
        Sort.Order order=new Sort.Order(Sort.Direction.ASC,"id");//mặc đinh sắp xếp tăng dần theo id
        if(StringUtils.hasLength(sortBy)){
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");// tencot:asc|desc
            Matcher matcher=pattern.matcher(sortBy);
            if(matcher.find()){
                String columnName = matcher.group(1);
                if(matcher.group(3).equalsIgnoreCase("asc")){
                    order=new Sort.Order(Sort.Direction.ASC,columnName);
                }else{
                    order=new Sort.Order(Sort.Direction.DESC,columnName);
                }
            }
        }

        //Paging
        Pageable pageable= PageRequest.of(page,pageSize,Sort.by(order));

        Page<User> usersPage =userRepository.findAllActiveUsers(pageable);

        List<UserResponse> userList = usersPage.stream().map(UserResponse::convert).toList();//chuyển hết sang userresponse

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(usersPage.getTotalPages())
                .items(userList)
                .build();
    }

    @Override
    public User createUser(UserCreationRequest request) {
        log.info("vào đến đây rồi");
        if(userRepository.existsByEmail(request.getEmail())){
            throw new BadCredentialsException("user đã tồn tại.1111");
        }

        Roles role =rolesRepository.findByName(String.valueOf(Role.USER))
                .orElseThrow(()-> new NotFoundException("role nay khong ton tai@@@"));


        User user=User.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .password(passwordEncoder.encode(request.getPassword()))
                .isActive(true)
                .facebookAccountId(request.getFacebookAccountId())
                .googleAccountId(request.getGoogleAccountId())
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
    public User update(Long userId,UserUpdateRequest request) {
        User user = findById(userId);

        user.setId(userId);
        //boolean isRequiredEmailVerification = false;

        if (StringUtils.hasText(String.valueOf(request.getDob())) && !request.getDob().equals(user.getBirthday())) {
            user.setBirthday(request.getDob());
        }//hmmm

        if (StringUtils.hasText(request.getPhoneNumber()) && !request.getPhoneNumber().equals(user.getPhoneNumber())) {//kiểm tra rỗng
            user.setPhoneNumber(request.getPhoneNumber());
        }

        if (StringUtils.hasText(request.getAvatarUrl()) && !request.getAvatarUrl().equals(user.getAvatarUrl())) {//kiểm tra rỗng
            user.setAvatarUrl(request.getAvatarUrl());
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
    public void delete(Long userId) {
        userRepository.deleteById(userId);
        // Get user by id
//        User user = findById(userId);
//        user.setActive(false);
//
//        userRepository.save(user);
    }

    @Override
    public User getMyInfo() {
        String email= SecurityUtils.getCurrentLogin()
                .orElseThrow(()->new BadCredentialsException("unauthorized"));
        log.info("email {}",email);
        User user=userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User not found"));
        return user;
    }
}
