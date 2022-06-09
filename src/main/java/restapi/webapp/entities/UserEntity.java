package restapi.webapp.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import restapi.webapp.converters.NameConvertor;

import javax.persistence.*;
import java.io.Serializable;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name="USERS")

public class UserEntity implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    private String email;
    private String md5;
    private String gender;
    private Integer age;
    private String phone;

//    @Column(name = "Name")
    //@Convert(converter = NameConvertor.class)
    //@Transient
    @Embedded
    private Name name;

    public UserEntity(String email, String md5, String gender, Integer age, String phone, Name name) {
        this.email = email;
        this.md5 = md5;
        this.gender = gender;
        this.age = age;
        this.phone = phone;
        this.name = name;
    }

//    @Convert(converter = LocationConvertor.class)
//    private Location location;
//
//    @Convert(converter = AvatarConvertor.class)
//    private Avatar avatar;

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
    public static class Location {
        private String city;
        private String street;
        private String state;
        private Integer postcode;
    }


    @Data
    public static class Avatar{
        private String seed;
        private String group;
        private Boolean glasses;
    }
}
