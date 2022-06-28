package restapi.webapp.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;


//insert using bean while running

@Data
@Entity
@NoArgsConstructor
@Table(name="CELL_PHONE_COMPANY")
public class CellPhoneCompany implements Serializable {
    @Id
    @GeneratedValue
    private Long cellPhoneCompanyId;
    @Column(nullable = false, unique = true)
    private String companyName;
    // insert random countries using api (?)
    @ElementCollection
    private Set<String> operationalCountries = new LinkedHashSet<>();

    public CellPhoneCompany(String companyName, Set<String> operationalCountries) {
        int randomAmountOfCountries = (int)Math.floor(Math.random()*(16-1+1)+1);
        String[] isoCountries = Locale.getISOCountries();
        for(int i = 0; i < randomAmountOfCountries; i++){
            int randomCountry =(int)Math.floor(Math.random()*(isoCountries.length-1+1)+1);
            Locale locale = new Locale("en", isoCountries[randomCountry]);
            this.operationalCountries.add(locale.getDisplayCountry());
        }
        this.companyName = companyName;
        this.operationalCountries.addAll(operationalCountries);
    }


    //
//    @JsonIgnore
//    @OneToMany(mappedBy = "cellphone_company")
//    private List<UserEntity> users = new ArrayList<>();





}
