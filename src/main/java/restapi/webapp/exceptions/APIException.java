package restapi.webapp.exceptions;

public class APIException extends RuntimeException{
    public APIException() {
        super("API is offline or experiencing server issues");
    }
}
