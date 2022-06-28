package restapi.webapp.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name="CELL PHONE COMPANY")
public class CellPhoneCompany implements Serializable {
    @Id @GeneratedValue
    private Long id;
    private String companyName;
    private List<String> operationalCountries;

    @JsonIgnore
    @OneToMany(mappedBy = "cellphone_company")
    private List<UserEntity> users = new ArrayList<>();


}
