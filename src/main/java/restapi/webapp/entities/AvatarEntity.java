package restapi.webapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;
import restapi.webapp.enums.HairColor;
import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * A class that represents the Avatar entity that contains its various characteristics and methods.
 */
@NoArgsConstructor
@Embeddable
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AvatarEntity implements Serializable {
    private String seed;
    @Min(1) @Max(26)
    private Integer eyes; // Range between 01-26
    @Min(1) @Max(10)
    private Integer eyebrows; // Range between 01-10

    private HairColor hairColor; // Enum
    @Min(1) @Max(30)
    private Integer mouth; // Range between 01-30

    private String resultUrl;

    public AvatarEntity(Integer eyes, Integer eyebrows, HairColor hairColor, Integer mouth) {
        this.eyes = eyes;
        this.eyebrows = eyebrows;
        this.hairColor = hairColor;
        this.mouth = mouth;
    }

    public String createResultUrl() {
        return String.format("https://avatars.dicebear.com/api/adventurer/%s.svg?eyes=variant%02d&eyebrows=variant%02d&hairColor=%s&mouth=variant%02d"
                , seed, eyes, eyebrows, hairColor, mouth);
    }
}
