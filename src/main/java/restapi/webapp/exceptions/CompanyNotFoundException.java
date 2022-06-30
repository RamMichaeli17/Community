package restapi.webapp.exceptions;

public class CompanyNotFoundException extends RuntimeException{
    public CompanyNotFoundException(String name) {
        super("Company " + name + " wasn't found.");
    }
}
