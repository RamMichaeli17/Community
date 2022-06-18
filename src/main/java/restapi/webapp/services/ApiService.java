package restapi.webapp.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import restapi.webapp.entities.UserEntity;
import restapi.webapp.entities.UserEntity.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class ApiService {
    private final ObjectMapper objectMapper;
    private final HashMap<String, String> userRetrieveTypes;

    public ApiService() {
        this.objectMapper = new ObjectMapper();

        userRetrieveTypes = new HashMap<>();
        userRetrieveTypes.put("random", "https://randomuser.me/api?exc=picture,cell,nat,registered&noinfo");
        userRetrieveTypes.put("male", "https://randomuser.me/api/?gender=male&exc=picture,cell,nat,registered&noinfo");
        userRetrieveTypes.put("female", "https://randomuser.me/api/?gender=female&exc=picture,cell,nat,registered&noinfo");
    }

    @SneakyThrows
    @Async
    public CompletableFuture<UserEntity> getUserByType(String userType) {
        //String jsonStringRepresentation = objectMapper.readValue(new URL(userRetrieveTypes.get(userType)), String.class);
        //System.out.println(jsonStringRepresentation);
       String jsonStringRepresentation = getStringFromNestedJsonFile(userRetrieveTypes.get(userType));
        if (jsonStringRepresentation!=null) {
            JSONObject rawJson = new JSONObject(jsonStringRepresentation);
            JSONArray jsonArrayToExtractUser = rawJson.getJSONArray("results");
            JSONObject userJson = jsonArrayToExtractUser.getJSONObject(0);

            // Extracting nested Location out of JSON
//            JSONObject locationJson = userJson.getJSONObject("location");
//            Location location = new Location
//                    (locationJson.getJSONObject("street").getString("name"),
//                            locationJson.getString("city"),
//                            locationJson.getString("state"));
//            // Removing "Location" key out of original JSON because it causes problems in de-serialization
//            userJson.remove("location");

            // Assigning nested values into the created user
            UserEntity user = objectMapper.readValue(userJson.toString(), UserEntity.class);

//            Integer age = userJson.getJSONObject("dob").getInt("age");
//            String md5 = userJson.getJSONObject("login").getString("md5");
//            user.setAge(age);
//            user.setMd5(md5);
//            user.setLocation(location);

            return CompletableFuture.completedFuture(user);
        }

        else {
            return CompletableFuture.failedFuture(new Throwable("Connection to API wasn't successful."));
        }
    }

    @SneakyThrows
    String getStringFromNestedJsonFile (String apiUrl) {
        StringBuilder builder = new StringBuilder();
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        if (conn.getResponseCode() == 200) {
            Scanner scan = new Scanner(url.openStream());
            while (scan.hasNext()) {
                builder.append(scan.nextLine());
            }
            return builder.toString();
        }
        else {
            return null;
        }
    }
}
