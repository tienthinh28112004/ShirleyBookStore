package ApiWebManga.config;


import ApiWebManga.Utils.LocalizationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class WebSecurityConfig {
    private final CustomJwtDecoder customJwtDecoder;

    private final LocalizationUtils localizationUtils;//dùng để chuyển thông tin anh,việt(làm màu là chính (\`.`/)

    private static final String[] White_List = {
            "/auth/**",
            "/books/getBook/{id}",
            "/books/books-search-criteria/**",
            "/books/books-search-keyword/**",
            "/books/bookList",
            "/chapter/getChapterById/{chapterId}",
            "/chapter/findPrevChapter/{bookId}/{chapterId}",
            "/chapter/findNextChapter/{bookId}/{chapterId}",
            "/chapter/findChaptersByBookId/{bookId}",
            "/chapter/getRecentChaptersWithElapsedTime",
            "/chapter/getLatestChapterAndTime/{bookId}",
            "/chapter/getRecentChapterByBookWithElapsedTime/{bookId}",
            "/category/getCategoryById/{categoryId}",
            "/comment/getCommentsByBook/{bookId}",
            "/payment/vn-pay-callback"
    };


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("public {}",White_List.length);
        http.csrf(AbstractHttpConfigurer::disable).cors(Customizer.withDefaults());//bật cors có cấu hình đặt ở bên dưới,bình thường nó sẽ không được bật
        http.authorizeHttpRequests(request -> request
                        .requestMatchers(White_List).permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS));

        log.info("alo");
        http.oauth2ResourceServer(oauth2->oauth2
                .jwt(jwtConfigurer -> jwtConfigurer.decoder(customJwtDecoder)//thuật toán để mã hóa token
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))//sau khi giải mã để lấy đựược jwt thì dùng cái anyf để phân quyền
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
//    **,Trình tự xử lý security
//      1,CorsFilter
//      2,JwtTokenFilter
//      3,WebSecurityConfig (SecurityFilterChain) ⬅ Chạy sau filter
//      4,Xử lý request trong controller.
}
