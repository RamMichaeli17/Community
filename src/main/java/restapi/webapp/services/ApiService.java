package restapi.webapp.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import restapi.webapp.entities.AvatarEntity;
import restapi.webapp.entities.UserEntity;
import restapi.webapp.exceptions.UserAPIException;
import restapi.webapp.assemblers.UserEntityAssembler;
import restapi.webapp.global.Utils;
import restapi.webapp.repos.UserRepo;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * A class that operates as the service of the API, containing the business logic of the operations
 * that are given by the external API.
 */
@Service
@Slf4j
public class ApiService {
    private final ObjectMapper objectMapper;
    private final Map<String, String> userRetrieveTypes;
    private final UserRepo userRepo;
    private final UserEntityAssembler assembler;

    @Autowired
    public ApiService(ObjectMapper objectMapper, UserRepo userRepo, UserEntityAssembler userEntityAssembler) {
        userRetrieveTypes = Map.of("random", "https://randomuser.me/api?exc=picture,cell,nat,registered&noinfo",
                "male", "https://randomuser.me/api/?gender=male&exc=picture,cell,nat,registered&noinfo",
                "female", "https://randomuser.me/api/?gender=female&exc=picture,cell,nat,registered&noinfo");
        this.objectMapper = objectMapper;
        this.userRepo = userRepo;
        this.assembler = userEntityAssembler;
    }

    /**
     * A function that fetches a random user from external API by requested type,
     * converts it into a UserEntity with all the needed values and returns it.
     * @param userType Type of user to be fetched from API (random/male/female).
     * @return CompletableFuture of UserEntity.
     */
    @SneakyThrows
    @Async
    public CompletableFuture<UserEntity> getUserByGender(@NonNull String userType) {
        log.info("Trying to fetch data from API");
        String jsonStringRepresentation = IOUtils.toString(new URL(userRetrieveTypes.get(userType)),
               StandardCharsets.UTF_8);
        log.info("Data fetched successfully");
        log.info("Trying to map JSON into a Java object");
        JSONObject rawJson = new JSONObject(jsonStringRepresentation);
        if (!rawJson.has("results")) { // There is an error
            return CompletableFuture.failedFuture(new UserAPIException());
        }
        JSONArray jsonArrayToExtractUser = rawJson.getJSONArray("results");
        JSONObject userJson = jsonArrayToExtractUser.getJSONObject(0);
        UserEntity user = objectMapper.readValue(userJson.toString(), UserEntity.class);
        user.setAvatarEntity(generateRandomAvatarEntity(user.getEmail()));
        user.getPhoneNumbers().addAll(generateRandomPhoneNumbers());
        log.info("Successfully mapped JSON into a Java object");
        return CompletableFuture.completedFuture(user);
    }

    /**
     * A method that gets a user entity as a parameter and saves it into the DB.
     * @param user User entity to be saved into the DB.
     * @return ResponseEntity of the saved user entity.
     */
    public ResponseEntity<?> saveUser(@NonNull UserEntity user) {
        userRepo.save(user);
        log.info("User {} has been created", user.getUserId());
        return ResponseEntity.of(Optional.of(assembler.toModel(user)));
    }

    /**
     * A function that generates random Avatar entity according to various characteristics.
     * The user inputs a seed that determines the basic looks of the avatar.
     * @param seed Seed to be injected into the AvatarEntity
     * @return A random AvatarEntity that was generated, based on seed.
     */
    public AvatarEntity generateRandomAvatarEntity(@NonNull String seed) {
        AvatarEntity randomAvatarForUser = new AvatarEntity();
        randomAvatarForUser.setSeed(seed);
        randomAvatarForUser.setEyes(Utils.randomNumberBetweenMinAndMax(1,26));
        randomAvatarForUser.setEyebrows(Utils.randomNumberBetweenMinAndMax(1,10));
        randomAvatarForUser.setMouth(Utils.randomNumberBetweenMinAndMax(1,30));
        randomAvatarForUser.setHairColor(Utils.randomHairColorFromEnum());
        randomAvatarForUser.setResultUrl(randomAvatarForUser.createResultUrl());
        return randomAvatarForUser;
    }

    /**
     * A method that generates random amount of cell phone numbers,
     * that will be inserted into a set of cell phone numbers that will be returned.
     * @return A set of random cell phone numbers
     */
    public Set<String> generateRandomPhoneNumbers() {
        Set<String> randomPhoneNumbers = new LinkedHashSet<>();
        int num1, num2, num3;
        DecimalFormat df3, df4;
        String phoneNumber;
        int randomAmountOfPhoneNumbers = Utils.randomNumberBetweenMinAndMax(0,20);
        Random rand = new Random();

        for(int i = 0; i<randomAmountOfPhoneNumbers; i++) {
            num1 = (rand.nextInt(7) + 1) * 100 + (rand.nextInt(8) * 10) + rand.nextInt(8);
            num2 = rand.nextInt(743);
            num3 = rand.nextInt(10000);
            df3 = new DecimalFormat("000"); // 3 digits in pattern
            df4 = new DecimalFormat("0000"); // 4 digits in pattern
            phoneNumber = df3.format(num1) + "-" + df3.format(num2) + "-" + df4.format(num3);
            randomPhoneNumbers.add(phoneNumber);
        }
        return randomPhoneNumbers;
    }

}
