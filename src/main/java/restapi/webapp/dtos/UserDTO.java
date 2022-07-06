package restapi.webapp.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Value;
import restapi.webapp.entities.CellPhoneCompanyEntity;
import restapi.webapp.entities.UserEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A class that represents the DTO form of a UserEntity.
 */
@Value
@JsonPropertyOrder({"Full Name","Email", "Age","Full Location","Avatar URL","Cell Phone Companies","Summary"})
public class UserDTO {

    @JsonIgnore
    private final UserEntity user;

    @JsonProperty("Full Name")
    public String getFullName() { return this.user.getName().getTitle()+" "+this.user.getName().getFirst() + " " + this.user.getName().getLast(); }

    @JsonProperty("Email")
    public String getEmail() { return this.user.getEmail(); }

    @JsonProperty("Age")
    public Integer getAge() { return this.user.getAge(); }

    @JsonProperty("Full Location")
    public String getFullLocation() { return this.user.getLocation().getStreet().getName()+" "+this.user.getLocation().getStreet().getNumber()+", "+this.user.getLocation().getCity()+", "+this.user.getLocation().getCountry(); }

    @JsonProperty("Avatar URL")
    public String getAvatarURL()  {return this.user.getAvatarEntity().getResultUrl();}

    @JsonProperty("Cell Phone Companies")
    public List<Map<String,String>> getCellPhoneCompanies() {
        List<Map<String,String>> cellPhonesCompaniesNamesAndID = new ArrayList<>();
//        return this.user.getCellPhoneCompanies().stream().map(CellPhoneCompanyEntity::getCompanyName).collect(Collectors.toList());
        for (CellPhoneCompanyEntity cellPhoneCompanyEntity : this.user.getCellPhoneCompanies()) {
            cellPhonesCompaniesNamesAndID.add(Map.of("companyName",cellPhoneCompanyEntity.getCompanyName(),"cellPhoneCompanyId",cellPhoneCompanyEntity.getCellPhoneCompanyId().toString()));
        }
        return cellPhonesCompaniesNamesAndID;
    }

    @JsonProperty("Summary")
    public String getSummary() {return String.format("Hello I'm %s. I'm %d years old and I live in %s. " +
            "This is my avatar: %s, if you want to learn more about this avatar or even share yours, " +
            "contact me at: %s.", this.getFullName(), this.getAge(), this.getFullLocation(),
            this.getAvatarURL(), this.getEmail());
    }
}
