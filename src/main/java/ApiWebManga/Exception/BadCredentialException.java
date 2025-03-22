package ApiWebManga.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class BadCredentialException extends RuntimeException{
    public BadCredentialException(){
        super("UNAUTHORIZED");
    }

    public BadCredentialException(String message){
        super(message);
    }

}
