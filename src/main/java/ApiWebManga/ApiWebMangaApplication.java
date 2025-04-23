package ApiWebManga;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;

@SpringBootApplication
@EnableFeignClients//mặc định feign sẽ tắt,ta phải khai báo nó ở đây thì ứng dụng mới dùng được
@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class ApiWebMangaApplication {

	public static void main(String[] args) {

		SpringApplication.run(ApiWebMangaApplication.class, args);
	}

}
