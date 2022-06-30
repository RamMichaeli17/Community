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
import restapi.webapp.enums.HairColor;
import restapi.webapp.factories.UserEntityAssembler;
import restapi.webapp.repos.UserRepo;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;

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

    @SneakyThrows
    @Async
    public CompletableFuture<UserEntity> getUserByType(@NonNull String userType) {
        log.info("Trying to fetch data from API");
       String jsonStringRepresentation = IOUtils.toString(new URL(userRetrieveTypes.get(userType)),
               StandardCharsets.UTF_8);
       log.info("Data fetched successfully");
       log.info("Trying to map JSON into a Java object");
        //todo: talk about this if statement
        if (jsonStringRepresentation!=null) {
            JSONObject rawJson = new JSONObject(jsonStringRepresentation);
            JSONArray jsonArrayToExtractUser = rawJson.getJSONArray("results");
            JSONObject userJson = jsonArrayToExtractUser.getJSONObject(0);
            UserEntity user = objectMapper.readValue(userJson.toString(), UserEntity.class);
            user.setAvatarEntity(generateRandomAvatarEntity(user.getEmail()));
            user.getPhoneNumbers().addAll(generateRandomPhoneNumbers());
            log.info("Successfully mapped JSON into a Java object");
            return CompletableFuture.completedFuture(user);
        }
        else {
            return CompletableFuture.failedFuture(new Throwable("Connection to API wasn't successful."));
        }
    }

    public ResponseEntity<?> saveUser(@NonNull UserEntity user) {
        userRepo.save(user);
        log.info("User {} has been created", user.getUserId());
        return ResponseEntity.of(Optional.of(assembler.toModel(user)));
    }

    public AvatarEntity generateRandomAvatarEntity(@NonNull String seed) {
        AvatarEntity randomAvatarForUser = new AvatarEntity();
        randomAvatarForUser.setSeed(seed);
        randomAvatarForUser.setEyes((int)Math.floor(Math.random()*(26-1+1)+1));
        randomAvatarForUser.setEyebrows((int)Math.floor(Math.random()*(10-1+1)+1));
        randomAvatarForUser.setMouth((int)Math.floor(Math.random()*(30-1+1)+1));
        final List<HairColor> HairColorValues = List.of(HairColor.values());
        final Random random = new Random();
        randomAvatarForUser.setHairColor(HairColorValues.get(random.nextInt(HairColorValues.size())));
        randomAvatarForUser.setResultUrl(randomAvatarForUser.createResultUrl());
        return randomAvatarForUser;
    }

    public Set<String> generateRandomPhoneNumbers() {
        Set<String> randomPhoneNumbers = new LinkedHashSet<>();
        int num1, num2, num3;
        DecimalFormat df3, df4;
        String phoneNumber;
        int randomAmountOfPhoneNumbers = (int)Math.floor(Math.random()*(20+1)+0);
        Random rand = new Random();
        for(int i=0; i<randomAmountOfPhoneNumbers; i++) {
            num1 = (rand.nextInt(7) + 1) * 100 + (rand.nextInt(8) * 10) + rand.nextInt(8);
            num2 = rand.nextInt(743);
            num3 = rand.nextInt(10000);
            df3 = new DecimalFormat("000"); // 3 zeros
            df4 = new DecimalFormat("0000"); // 4 zeros
            phoneNumber = df3.format(num1) + "-" + df3.format(num2) + "-" + df4.format(num3);
            randomPhoneNumbers.add(phoneNumber);
        }
        return randomPhoneNumbers;
    }

}
