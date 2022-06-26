package restapi.webapp.exceptions.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import restapi.webapp.exceptions.UserNotFoundException;

@ControllerAdvice

public class GlobalAdvice {
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    String userNotFoundHandler(UserNotFoundException unfe){
        return unfe.getMessage();
    }
}
