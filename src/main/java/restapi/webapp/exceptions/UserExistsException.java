package restapi.webapp.exceptions;

public class UserExistsException extends RuntimeException{
    public UserExistsException(String email) {
        super("User with email " + email + " is already exists.");
    }
}