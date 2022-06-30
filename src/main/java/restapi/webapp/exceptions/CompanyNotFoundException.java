package restapi.webapp.exceptions;

public class CompanyNotFoundException extends RuntimeException{
    public CompanyNotFoundException(Long id) {
        super("Company " + id + " wasn't found.");
    }
    public CompanyNotFoundException(String name) {
        super("Company " + name + " wasn't found.");
    }
}
