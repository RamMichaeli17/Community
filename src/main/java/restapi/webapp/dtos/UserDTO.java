package restapi.webapp.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;
import restapi.webapp.entities.UserEntity;
import restapi.webapp.entities.UserEntity.*;

@Value
public class UserDTO {

    @JsonIgnore
    final private UserEntity user;

    public String getName() { return this.user.getName().getFirst(); }

}
