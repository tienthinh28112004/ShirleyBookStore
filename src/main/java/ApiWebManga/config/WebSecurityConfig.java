package ApiWebManga.config;


import ApiWebManga.Utils.LocalizationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    @Lazy
    private CustomJwtDecoder customJwtDecoder;

    private final UserDetailsService userDetailsService;
    private final LocalizationUtils localizationUtils;//dùng để chuyển thông tin anh,việt(làm màu là chính (\`.`/)

    private static final String[] White_List = {
            "/apiMangaWeb/auth/**",
            "/apiMangaWeb/auth/register",
            "/apiMangaWeb/refresh",
            "/apiMangaWeb/users-creation",
            "/apiMangaWeb/books",
            "/apiMangaWeb/books-search-criteria/**",
            "/apiMangaWeb/books-search-keyword/**",
            "/apiMangaWeb/books/{id}"
    };
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request.//requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll().
                        requestMatchers("/**").permitAll()
//                        .requestMatchers("/public/**", "/auth/login", "/auth/register").permitAll()
//                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
//                        .requestMatchers("/user/**").hasAnyAuthority("USER", "ADMIN")
                        .anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS));

        http.oauth2ResourceServer(oauth2->oauth2
                .jwt(jwtConfigurer -> jwtConfigurer.decoder(customJwtDecoder)//thuật toán để mã hóa token
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))//sau khi giải mã để lấy đựược jwt thì dùng cái anyf để phân quyền
                        //.jwtAuthenticationConverter(jwtAuthenticationConverter()
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint(localizationUtils))//bắt lỗi đăng nhập không thành công 401(unauthenticate)
                .accessDeniedHandler(new JwtAccessDined(localizationUtils)));//bắt lỗi không có quyền 403(forbiden)

        return http.build();
    }
    @Bean
    public WebSecurityCustomizer ignoreResources() {
        return webSecurity -> webSecurity
                .ignoring()
                .requestMatchers("/actuator/**", "/v3/**", "/webjars/**", "/swagger-ui*/*swagger-initializer.js", "/swagger-ui*/**", "/favicon.ico");
    }
    @Bean
    public PasswordEncoder PasswordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter=new JwtGrantedAuthoritiesConverter();//chuyển hết đối tượng thành granted để phân quyền
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("Authority");//mặc điịnh là nó đọc scope trong jwt nhưng ở đây convert cho nó đọc Authority trong jwt
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");//loại bỏ tiền tố _SCOPE

        JwtAuthenticationConverter jwtAuthenticationConverter= new JwtAuthenticationConverter();//chuyển jwt thành authenticattion
        //lúc này mặc định pripical sẽ lấy subject(email) trong jwt và minhf sẽ gán thêm quyền cho nó
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);//set quyền vừa tạo được ở bên trên cho nó
        return jwtAuthenticationConverter;
    }
    @Bean//xem lại sau
    public CorsFilter corsFilter () {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);//nếu mốn dùng httponly Cookie thì bắt buộc phải có dòng này
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type","Accept-Language", "x-no-retry"));
        configuration.setExposedHeaders(List.of("Authorization", "Content-Type", "Accept-Language"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source);
    }

//    **,Trình tự xử lý security
//      1,CorsFilter
//      2,JwtTokenFilter
//      3,WebSecurityConfig (SecurityFilterChain) ⬅ Chạy sau filter
//      4,Xử lý request trong controller.
}
