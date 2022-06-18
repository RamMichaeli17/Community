package restapi.webapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import restapi.webapp.enums.AvatarGroups;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name="USERS")
@JsonIgnoreProperties(ignoreUnknown = true, value={"id"})
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue
    //ToDo: why ?
    private Long userId;
    @Column(nullable = false, unique = true)
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

    public UserEntity(String email, String md5, String gender, Integer age,
                      String phone, Name name, Location location, Avatar avatar) {
        this.email = email;
        this.md5 = md5;
        this.gender = gender;
        this.age = age;
        this.phone = phone;
        this.name = name;
        this.location = location;
        this.avatar = avatar;
    }

    @JsonProperty("dob")
    private void unpackAge(Map<String, Object> dob) {
        this.age = Integer.parseInt(dob.get("age").toString());
    }

    @JsonProperty("login")
    private void unpackMD5(Map<String, Object> login) {
        this.md5 = login.get("md5").toString();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class Name {
        private String title;
        private String first;
        private String last;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Location {
        private String city;
        private Street street;
        private String country;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        @Embeddable
        public static class Street {
            private String name;
            private String number;
        }
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
