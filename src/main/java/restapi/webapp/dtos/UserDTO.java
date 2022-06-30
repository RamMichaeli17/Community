package restapi.webapp.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;
import restapi.webapp.entities.UserEntity;
import restapi.webapp.entities.UserEntity.*;

import java.net.MalformedURLException;
import java.net.URL;

@Value
@JsonPropertyOrder({"Full Name","Email", "Age","Full Location","Avatar URL","Summary"})
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

    @JsonProperty("Summary")
    public String getSummary() {return String.format("Hello I'm %s. I'm %d years old and I live in %s." +
            "This is my avatar: %s, if you want to learn more about this avatar or even share yours, " +
            "contact me at: %s.", this.getFullName(), this.getAge(), this.getFullLocation(),
            this.getAvatarURL(), this.getEmail());
    }
}
