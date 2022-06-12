package restapi.webapp.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import restapi.webapp.entities.UserEntity;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class ApiService {
    @Autowired
    private RestTemplate template;

    public ApiService(RestTemplateBuilder restTemplateBuilder) {
        this.template = restTemplateBuilder.build();
    }

    @Async
    public CompletableFuture<UserEntity> getRandomUser() {
        String[] genders = {"male", "female"};
        Random random = new Random();
        int selection = random.nextInt(genders.length);

        String urlTemplate = String.format("https://randomuser.me/api/?gender=%s", genders[selection]);
        //String urlTemplate = "https://randomuser.me/api/?gender=female";
        UserEntity user = this.template.getForObject(urlTemplate, UserEntity.class);

        return CompletableFuture.completedFuture(user);
    }

    @Async
    public CompletableFuture<UserEntity> getMaleUser() {
        String urlTemplate = "https://randomuser.me/api/?gender=male";
        UserEntity user = this.template.getForObject(urlTemplate, UserEntity.class);

        return CompletableFuture.completedFuture(user);
    }

    @Async
    public CompletableFuture<UserEntity> getFemaleUser() {
        String urlTemplate = "https://randomuser.me/api/?gender=female";
        UserEntity user = this.template.getForObject(urlTemplate, UserEntity.class);

        return CompletableFuture.completedFuture(user);
    }
}
