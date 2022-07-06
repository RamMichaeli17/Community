package restapi.webapp.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long id) {
        super("User with ID " + id + " was not found.");
    }
    public UserNotFoundException(String value) {
        super("User " + value + " was not found.");
    }
    public UserNotFoundException(String param, String value){
        super("User with param " + param + " and value " + value + " was not found.");
    }
}
