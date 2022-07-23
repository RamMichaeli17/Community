package restapi.webapp.global;

import restapi.webapp.enums.HairColor;

import java.util.List;
import java.util.Random;

/**
 * A helper class that provides methods that are widely used throughout the whole project.
 */
public class Utils {
    /**
     * A method that returns a random number between ranges.
     * @param min Minimum number in range.
     * @param max Maximum number in range.
     * @return Randomised integer number between specified range.
     */
    public static Integer randomNumberBetweenMinAndMax(int min, int max) {
       return (int)(Math.random()*(max-min+1)+min);
    }

    /**
     * A method that returns a random hair color.
     * @return Randomised HairColor type.
     */
    public static HairColor randomHairColorFromEnum() {
        final List<HairColor> HairColorValues = List.of(HairColor.values());
        final Random random = new Random();
        return HairColorValues.get(random.nextInt(HairColorValues.size()));
    }
}
