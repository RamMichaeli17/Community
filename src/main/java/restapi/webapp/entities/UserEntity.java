package restapi.webapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * A class that represents the User entity, and contains its various characteristics and methods
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name="USERS")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long userId;
    @Column(nullable = false, unique = true)
    private String email;
    private String md5;
    private String gender;
    private Integer age;
    @ElementCollection
    private Set<String> phoneNumbers = new LinkedHashSet<>();
    @Embedded
    private Name name;
    @Embedded
    private Location location;

    private AvatarEntity avatarEntity;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "Users_Companies",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "company_id")})
    Set<CellPhoneCompany> cellPhoneCompanies = new LinkedHashSet<>();

    public UserEntity(String email, String md5, String gender, Integer age,
                      Set<String> phoneNumbers, Name name, Location location, AvatarEntity avatarEntity) {
        this.email = email;
        this.md5 = md5;
        this.gender = gender;
        this.age = age;
        this.phoneNumbers = phoneNumbers;
        this.name = name;
        this.location = location;
        this.avatarEntity = avatarEntity;
        this.avatarEntity.setSeed(email);
        this.avatarEntity.setResultUrl(this.avatarEntity.createResultUrl());
    }

    @JsonProperty("dob")
    private void unpackAge(Map<String, Object> dob) {
        this.age = Integer.parseInt(dob.get("age").toString());
    }

    @JsonProperty("login")
    private void unpackMD5(Map<String, Object> login) {
        this.md5 = login.get("md5").toString();
    }
    @JsonProperty("phone")
    private void addPhoneToSet(String phone) {
        this.phoneNumbers.add(phone);
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
}
