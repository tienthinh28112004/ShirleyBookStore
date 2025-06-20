package ApiWebManga.Entity;

import ApiWebManga.Enums.RegistrationStatus;
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
@Table(name = "users")
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

    @Column(name = "is_active")
    boolean isActive;

    @Column(name ="avatar_url")
    private String avatarUrl;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private LocalDate birthday;

    @Column(name = "expertise")
    private String expertise;

    @Column(name = "yearsOfExperience")
    private Double yearsOfExperience;

    @Column(name = "bio")
    private String bio;

    @Column(name = "certificate")
    private String certificate;

    @Column(name = "cvUrl")
    private String cvUrl;

    @Column(name = "facebookLink")
    private String facebookLink;

    @Column(name = "registrationStatus")
    @Enumerated(EnumType.STRING)
    private RegistrationStatus registrationStatus;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private EmailVerificationToken emailVerificationToken;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Cart cart;

    @Column(name = "email_verified_at")
    private LocalDateTime emailVerifiedAt;

    @OneToMany(mappedBy = "author",cascade = CascadeType.ALL)
    private List<Book> books;//cái này là có tác dụng với người là tác giả truyện thôi(chứ bth thì không áp dụng với các user bình thương)

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference//để phòng hờ thôi vì chủ yếu mình đưa ra userReponse mà
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<UserHasRoles> userHasRoles;

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
