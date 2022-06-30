package restapi.webapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Data
@Entity
@NoArgsConstructor
@Table(name="CELL_PHONE_COMPANY")
public class CellPhoneCompany implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cellPhoneCompanyId;

    @Column(nullable = false, unique = true)
    private String companyName;

    @ElementCollection
    private Set<String> operationalCountries = new LinkedHashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "cellPhoneCompanies")
    private Set<UserEntity> users = new LinkedHashSet<>();

    public CellPhoneCompany(String companyName, Set<String> operationalCountries) {
        //todo: extract this function to public class: utilities
        int randomAmountOfCountries = (int)Math.floor(Math.random()*(12-1+1)+1);

        String[] isoCountries = Locale.getISOCountries();
        for(int i = 0; i < randomAmountOfCountries-1; i++){
            int randomCountry = (int)Math.floor(Math.random()*(isoCountries.length-1+1)+1);
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
