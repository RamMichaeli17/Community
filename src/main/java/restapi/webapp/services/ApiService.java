package restapi.webapp.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import restapi.webapp.entities.UserEntity;
import restapi.webapp.entities.UserEntity.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class ApiService {
    private final ObjectMapper objectMapper;
    private final Map<String, String> userRetrieveTypes;

    public ApiService() {
        this.objectMapper = new ObjectMapper();
        userRetrieveTypes = Map.of("random", "https://randomuser.me/api?exc=picture,cell,nat,registered&noinfo",
                "male", "https://randomuser.me/api/?gender=male&exc=picture,cell,nat,registered&noinfo",
                "female", "https://randomuser.me/api/?gender=female&exc=picture,cell,nat,registered&noinfo");
    }

    @SneakyThrows
    @Async
    public CompletableFuture<UserEntity> getUserByType(String userType) {
       String jsonStringRepresentation = IOUtils.toString(new URL(userRetrieveTypes.get(userType)), StandardCharsets.UTF_8);
        if (jsonStringRepresentation!=null) {
            JSONObject rawJson = new JSONObject(jsonStringRepresentation);
            JSONArray jsonArrayToExtractUser = rawJson.getJSONArray("results");
            JSONObject userJson = jsonArrayToExtractUser.getJSONObject(0);
            UserEntity user = objectMapper.readValue(userJson.toString(), UserEntity.class);
            return CompletableFuture.completedFuture(user);
        }
        else {
            return CompletableFuture.failedFuture(new Throwable("Connection to API wasn't successful."));
        }
    }

}
