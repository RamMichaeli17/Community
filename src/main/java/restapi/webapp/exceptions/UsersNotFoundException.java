package restapi.webapp.exceptions;

public class UsersNotFoundException extends RuntimeException{
    public UsersNotFoundException() {
        super("Users not found on DB.");
    }
}
