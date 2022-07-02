package restapi.webapp.exceptions;

public class UserAPIException extends RuntimeException{
    public UserAPIException() {
        super("API is offline or experiencing server issues");
    }
}
