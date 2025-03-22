package ApiWebManga.config;

import ApiWebManga.Entity.Roles;
import ApiWebManga.Entity.User;
import ApiWebManga.Entity.UserHasRoles;
import ApiWebManga.Enums.Role;
import ApiWebManga.repository.RolesRepository;
import ApiWebManga.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class ApplicationInitConfig {

    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driver-class-name",
            havingValue = "com.mysql.cj.jdbc.Driver")//điều kiện này phải đúng thì nó mới ược khởi tạo(đối chiếu trong yaml)
    ApplicationRunner initApplication() {
        log.info("Initializing application.....");
        return args -> {
            Optional<Roles> roleUser = rolesRepository.findByName(String.valueOf(Role.USER));
            if(roleUser.isEmpty()) {
                rolesRepository.save(Roles.builder()
                        .name(String.valueOf(Role.USER))
                        .description("User role")
                        .build());
            }

            Optional<Roles> roleAdmin = rolesRepository.findByName(String.valueOf(Role.ADMIN));
            if(roleAdmin.isEmpty()) {
                rolesRepository.save(Roles.builder()
                    .name(String.valueOf(Role.ADMIN))
                    .description("Admin role")
                    .build());
                //tạo tài khoản admin
                User user= User.builder()
                        .email("admin@gmail.com")
                        .password("Admin@123")
                        .build();
                Roles role=Roles.builder().name(Role.ADMIN.name()).build();
                Set<UserHasRoles> userHasRoles=new HashSet<>();
                userHasRoles.add(UserHasRoles.builder()
                        .user(user)
                        .role(role)
                        .build());
                user.setUserHasRoles(userHasRoles);
                userRepository.save(user);
            }

            Optional<Roles> roleManager = rolesRepository.findByName(String.valueOf(Role.MANAGER));
            if(roleManager.isEmpty()) {
                rolesRepository.save(Roles.builder()
                        .name(String.valueOf(Role.MANAGER))
                        .description("Manager role")
                        .build());
            }
            System.out.println("vào đây rồi");
            Optional<Roles> roleStaff = rolesRepository.findByName(String.valueOf(Role.STAFF));
            if(roleStaff.isEmpty()) {
                rolesRepository.save(Roles.builder()
                        .name(String.valueOf(Role.STAFF))
                        .description("Staff role")
                        .build());
            }
            log.info("Application initialization completed .....");
        };
    }
}
