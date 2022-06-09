package entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import utilities.converters.NameConvertor;

import javax.persistence.*;


@NoArgsConstructor
@Entity
@Data
@Table(name="Users")

public class User {

    @Id
    @GeneratedValue
    private String id;

    @Id
    private String email;

    private String md5;
    private String gender;
    private Integer age;
    private String phone;

//    @Column(name = "Name")
    @Convert(converter = NameConvertor.class)
    private Name name;

//    @Convert(converter = LocationConvertor.class)
//    private Location location;
//
//    @Convert(converter = AvatarConvertor.class)
//    private Avatar avatar;

    @Data
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
