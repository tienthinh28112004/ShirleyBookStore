package ApiWebManga.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfiguration {
    @Bean//postman vẫn dùng bình thường vì gửi thẳng còn cái này cấu hình cho việc gửi từ các domain khác
    public CorsFilter corsFilter () {
        org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();
        configuration.setAllowCredentials(true);//nếu mốn dùng httponly Cookie thì bắt buộc phải có dòng này
        configuration.setAllowedOrigins(List.of("http://localhost:3000","http://localhost:3002","http://localhost:3001"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS","PATCH"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type","Accept-Language", "x-no-retry","Access-Control-Allow-Origin"));
        configuration.setExposedHeaders(List.of("Authorization", "Content-Type", "Accept-Language"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source);
    }

}
