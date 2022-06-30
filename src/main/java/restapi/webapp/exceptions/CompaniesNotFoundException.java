package restapi.webapp.exceptions;

public class CompaniesNotFoundException extends RuntimeException{
    public CompaniesNotFoundException() {
        super("Companies not found on DB.");
    }
}
