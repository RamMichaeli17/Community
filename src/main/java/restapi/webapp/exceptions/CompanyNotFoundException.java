package restapi.webapp.exceptions;

public class CompanyNotFoundException extends RuntimeException{
    public CompanyNotFoundException(Long id) {
        super("Company with ID " + id + " wasn't found.");
    }
    public CompanyNotFoundException(String value) {
        super("Company named " + value + " wasn't found.");
    }
}
