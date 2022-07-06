package restapi.webapp.exceptions;

public class UsersNotFoundException extends RuntimeException{
    public UsersNotFoundException() {
        super("Users not found on DB.");
    }
    public UsersNotFoundException(String param, String value){
        super("Users with param " + param + " and value " + value + " were not found.");
    }
    public UsersNotFoundException(String value) {
        super("Users with " + value + " were not found.");
    }
}
