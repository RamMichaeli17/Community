package restapi.webapp.exceptions.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import restapi.webapp.exceptions.*;

import java.net.UnknownHostException;

@ControllerAdvice
public class GlobalAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    String userNotFoundHandler(UserNotFoundException unfe){
        return unfe.getMessage();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UsersNotFoundException.class)
    String usersNotFoundHandler(UsersNotFoundException unfe){
        return unfe.getMessage();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CompanyNotFoundException.class)
    String userNotFoundHandler(CompanyNotFoundException cnfe){
        return cnfe.getMessage();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CompaniesNotFoundException.class)
    String companiesNotFoundHandler(CompaniesNotFoundException cnfe){
        return cnfe.getMessage();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(UnknownHostException.class)
    String ApiNotReachableHandler(UnknownHostException uhe){
        return String.format("API in `%s` is unreachable for this moment", uhe.getMessage());
    }

}
