package restapi.webapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * A class that represents the Cell Phone Company entity, and contains its various characteristics and methods.
 */
@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Table(name="CELL_PHONE_COMPANY")
public class CellPhoneCompanyEntity implements Serializable {
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

    public CellPhoneCompanyEntity(String companyName, Set<String> operationalCountries) {
        //todo: extract this function to public class: utilities
        String[] isoCountries = Locale.getISOCountries();
        int randomAmountOfCountries = (int) ((Math.random() * (12-1)) + 1);

        for(int i = 0; i < randomAmountOfCountries; i++){
            int randomCountry = (int) ((Math.random() * (isoCountries.length-1 - 1)) + 1);

            Locale locale = new Locale("en", isoCountries[randomCountry]);
            this.operationalCountries.add(locale.getDisplayCountry());
        }

        this.companyName = companyName;
        this.operationalCountries.addAll(operationalCountries);
    }

    @Override
    public String toString() {
        return "CellPhoneCompanyEntity{" +
                "cellPhoneCompanyId=" + cellPhoneCompanyId +
                ", companyName='" + companyName + '\'' +
                ", operationalCountries=" + operationalCountries +
                '}';
    }
}
