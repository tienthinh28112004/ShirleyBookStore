package ApiWebManga.Utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;


@Component
@RequiredArgsConstructor
public class LocalizationUtils {
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

    public String getLocalizedMessage(String messageKey,Object ...params){//params nếu không truyền vào sẽ là mảng rỗng và không ảnh hưởng đến code
        HttpServletRequest request = WebUtils.getCurrentRequest();
        Locale local = localeResolver.resolveLocale(request);//lấy ra thông tin ở request(để xác định vi hay en)
        return messageSource.getMessage(messageKey,params,local);
    }
}
