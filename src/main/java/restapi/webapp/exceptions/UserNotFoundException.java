package restapi.webapp.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long id) {
        super("User with ID " + id + " wasn't found.");
    }
    public UserNotFoundException(String value) {
        super("User " + value + " wasn't found.");
    }
}
