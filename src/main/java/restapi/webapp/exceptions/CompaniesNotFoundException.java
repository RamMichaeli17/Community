package restapi.webapp.exceptions;

public class CompaniesNotFoundException extends RuntimeException{
    public CompaniesNotFoundException() {
        super("Cell phone companies not found on DB.");
    }
}
