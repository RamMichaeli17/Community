package restapi.webapp.exceptions;

public class CompanyExistsException extends RuntimeException{
    public CompanyExistsException(String name) {
        super("Company with name " + name + " already exists.");
    }
}