package ApiWebManga.service.Impl;

import ApiWebManga.Exception.NotFoundException;
import ApiWebManga.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceCustomizer implements UserDetailsService {
    //thay vì nếu SecurityContextHolder.getContext().authentication.getPrincipal() là userDetails thì nó sẽ trả về userDetail.getUserName() là username của đối tượng
    //thì ở đây ta customize cho nó để giá trị trả về của userDetail.getUserName() lúc này chính là email mà chúng ta dùng để đăng nhập
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new NotFoundException("User not found"));
        //User details do đã được kế thừa trong User nên ở đây có thể trả về UserDetail thoải mái
    }
}
