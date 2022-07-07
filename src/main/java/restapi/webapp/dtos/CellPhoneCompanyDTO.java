package restapi.webapp.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Value;
import restapi.webapp.entities.CellPhoneCompanyEntity;

/**
 * A class that represents the DTO form of a cell phone company.
 */
@Value
@JsonPropertyOrder({"Company Name","Summary"})
public class CellPhoneCompanyDTO {

    @JsonIgnore
    private final CellPhoneCompanyEntity cellPhoneCompany;

    @JsonProperty("Company Name")
    public String getCompanyName() {
        return this.cellPhoneCompany.getCompanyName();
    }

    //TODO: change specific number to number of users
    @JsonProperty("Summary")
    public String getSummary() {
        return String.format("%s company operates in several countries: %s. The company currently has %d users.",this.getCompanyName(),cellPhoneCompany.getOperationalCountries(),4 );
    }
}
