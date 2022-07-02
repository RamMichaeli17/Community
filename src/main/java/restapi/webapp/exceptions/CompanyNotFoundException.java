package restapi.webapp.exceptions;

public class CompanyNotFoundException extends RuntimeException{
    public CompanyNotFoundException(Long id) {
        super("Company with " + id + " wasn't found.");
    }
    public CompanyNotFoundException(String value) {
        super("Company with " + value + " wasn't found.");
    }
}
