package ApiWebManga.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException{
    public NotFoundException(){
        super("Not Found");
    }

    public NotFoundException(String message){
        super(message);
    }
}
