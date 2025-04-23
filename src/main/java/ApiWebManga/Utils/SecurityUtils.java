package ApiWebManga.Utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;


public class SecurityUtils {
    private SecurityUtils(){//đây là lớp tiện iích chỉ hỗ trợ xử lí các phương pháp chung
    }//nên phải có cái này để tránh khai báo được ecurityUtils utils = new SecurityUtils(); từ bên ngoài
    public static Optional<String> getCurrentLogin(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication=context.getAuthentication();

        if(authentication.getPrincipal() instanceof UserDetails userDetails){
            //nếu authentication.getPrincipal() thuộc kiểu UserDetails của spring security thì trả ra Username(ở đây Username của UserDeatil chính là email
            return Optional.ofNullable(userDetails.getUsername());//chính là email
        }
        if(authentication.getPrincipal() instanceof Jwt jwt){//jwt của oauth2
            //nếu authentication.getPrincipal() thuộc kiểu Jwt thì trả ra gétubject
           return Optional.ofNullable(jwt.getSubject());//lấy ra email
        }
        if(authentication.getPrincipal() instanceof String s){
            //nếu authentication.getPrincipal() thuộc kiểu String thì trả ra
            return Optional.of(s);
        }
        //tất cả đều trả ra email
        return Optional.empty();
    }
}
