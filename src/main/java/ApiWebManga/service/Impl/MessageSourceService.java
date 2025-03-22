package ApiWebManga.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageSourceService {//trả về thông baáo cho fontend
    private final MessageSource messageSource;


    public String get(final String code, final Object[] params, final Locale locale) {
        //hàm xử lí chính,3 hàm sau chỉ để chạy vào hàm này
        try {
            return messageSource.getMessage(code, params, locale);
        } catch (NoSuchMessageException e) {
            log.warn("Translation message not found ({}): {}", locale, code);
            return code;
        }
    }

    public String get(String code,Object[] params){
        return get(code,params, LocaleContextHolder.getLocale());//chuyển ngôn ngữ địa phương
    }

    private String get(String code,Locale locale){
        return get(code,new Object[0],locale);
    }

    public String get(String code){
        return get(code,new Object[0],LocaleContextHolder.getLocale());//chuyển ngôn ngữ địa phương
    }
}
