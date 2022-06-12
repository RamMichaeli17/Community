package restapi.webapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import restapi.webapp.enums.AvatarGroups;
import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name="USERS")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserEntity implements Serializable {

    @Id
    private String email;
    private String md5;
    private String gender;
    private Integer age;
    private String phone;

    @Embedded
    private Name name;

    @Embedded
    private Location location;

    @Embedded
    private Avatar avatar;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class Name {
        private String title;
        private String firstName;
        private String lastName;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class Location {
        private String city;
        private String street;
        private String state;
        private Integer postcode;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class Avatar {
        private AvatarGroups avatarGroup;
        private Boolean glasses;
    }
}
