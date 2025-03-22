package ApiWebManga.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends AbstractEntity<Long> implements UserDetails {
    @Column(name = "fullName", length = 255)
    String fullName;

    @Column(name = "email", length = 255)
    String email;

    @Column(name = "password", length = 255)
    String password;

    @Column(name = "phone")
    String phoneNumber;

    @Column(name = "refresh_token", columnDefinition = "TEXT")
    String refreshToken;

    @Column(name = "facebook_account_id")
    private int facebookAccountId;

    @Column(name = "google_account_id")
    private int googleAccountId;

//    @Enumerated(EnumType.STRING)
//    @Column(name = "gender")
//    Gender gender;

    @Column(name = "is_active")
    boolean isActive;

    @Column(name ="avatar_url")
    private String avatarUrl;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    LocalDate birthday;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private EmailVerificationToken emailVerificationToken;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    @Column(name = "email_verified_at")
    private LocalDateTime emailVerifiedAt;

    @OneToMany(mappedBy = "author",cascade = CascadeType.ALL)
    private List<Book> books;//cái này là có tác dụng với người là tác giả truyện thôi(chứ bth thì không áp dụng với các user bình thương)

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserHasRoles> userHasRoles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference//được đặt ở phía cha khi chuyển thành json sẽ được hiển thị trong json
    private List<Comment> comments = new ArrayList<>();

    //các phương thức dưới là các phương thức kết thwuaf được từ các userdetail
    @Override//phân role và vai trò tất cả ở trong này
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //trả về danh sách các quyền của ngời dùng
        return userHasRoles.stream().map(userHasRole->userHasRole.getRole())
                .map(roles -> new SimpleGrantedAuthority(roles.getName()))
                .collect(Collectors.toSet());
        //Chuyển tên vai trò thành một GrantedAuthority, giúp Spring Security hiểu và kiểm tra quyền của người dùng.
        //ví dụ role là ADMIN thì nó sẽ tạo GrantedAuthority cps gái trị "ADMIN"
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
