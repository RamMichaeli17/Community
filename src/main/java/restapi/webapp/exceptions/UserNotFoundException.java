package restapi.webapp.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long id) {
        super("User " + id + " wasn't found.");
    }
    public UserNotFoundException(String email) {
        super("User " + email + " wasn't found.");
    }
}
