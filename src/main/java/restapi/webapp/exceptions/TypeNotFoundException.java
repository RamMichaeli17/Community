package restapi.webapp.exceptions;

public class TypeNotFoundException extends RuntimeException{
    public TypeNotFoundException(String type) {
        super("Type " + type + " is not valid.");
    }
}
