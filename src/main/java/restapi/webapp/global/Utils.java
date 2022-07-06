package restapi.webapp.global;

import restapi.webapp.enums.HairColor;

import java.util.List;
import java.util.Random;

public class Utils {
    public static Integer randomNumberBetweenMinAndMax(int min, int max) {
       return (int)(Math.random()*(max-min+1)+min);
    }
    public static HairColor randomHairColorFromEnum() {
        final List<HairColor> HairColorValues = List.of(HairColor.values());
        final Random random = new Random();
        return HairColorValues.get(random.nextInt(HairColorValues.size()));
    }
}
