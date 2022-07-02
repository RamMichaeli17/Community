package restapi.webapp.exceptions.advices;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import restapi.webapp.exceptions.*;

import java.net.UnknownHostException;

@ControllerAdvice
@Slf4j
public class GlobalAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserExistsException.class)
    String UserExistsHandler(UserExistsException uee){
        String message = uee.getMessage();
        log.error(message);
        return message;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    String userNotFoundHandler(UserNotFoundException unfe){
        String message = unfe.getMessage();
        log.error(message);
        return message;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UsersNotFoundException.class)
    String usersNotFoundHandler(UsersNotFoundException unfe){
        String message = unfe.getMessage();
        log.error(message);
        return message;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(CompanyExistsException.class)
    String CompanyExistsHandler(CompanyExistsException cee){
        String message = cee.getMessage();
        log.error(message);
        return message;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CompanyNotFoundException.class)
    String userNotFoundHandler(CompanyNotFoundException cnfe){
        String message = cnfe.getMessage();
        log.error(message);
        return message;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CompaniesNotFoundException.class)
    String companiesNotFoundHandler(CompaniesNotFoundException cnfe){
        String message = cnfe.getMessage();
        log.error(message);
        return message;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(UnknownHostException.class)
    String ApiNotReachableHandler(UnknownHostException uhe){
        return String.format("API in `%s` is unreachable for this moment", uhe.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(UserAPIException.class)
    String UserAPIHandler(UserAPIException uapie){
        String message = uapie.getMessage();
        log.error(message);
        return message;
    }
}
