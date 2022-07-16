package restapi.webapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import restapi.webapp.global.Utils;
import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * A class that represents the Cell Phone Company entity, and contains its various characteristics and methods.
 */
@Data
@Entity
@NoArgsConstructor
@Table(name="CELL_PHONE_COMPANIES")
public class CellPhoneCompanyEntity implements Serializable {
    @Id
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cellPhoneCompanyId;

    @Column(nullable = false, unique = true)
    private String companyName;

    @ElementCollection
    private Set<String> operationalCountries = new LinkedHashSet<>();

    @JsonIgnore
    @ManyToMany
    private Set<UserEntity> users = new LinkedHashSet<>();

    public CellPhoneCompanyEntity(String companyName, Set<String> operationalCountries) {
        String[] isoCountries = Locale.getISOCountries();
        int randomAmountOfCountries = Utils.randomNumberBetweenMinAndMax(1,12);

        for(int i = 0; i < randomAmountOfCountries; i++){
            int randomCountry = Utils.randomNumberBetweenMinAndMax(1, isoCountries.length-1);

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
