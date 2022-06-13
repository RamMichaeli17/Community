package restapi.webapp.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import restapi.webapp.entities.UserEntity;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class ApiService {
    private RestTemplate template;
    public ApiService(RestTemplateBuilder restTemplateBuilder) {
        this.template = restTemplateBuilder.build();
    }

    @Async
    public CompletableFuture<UserEntity> getRandomUser() {
        String urlTemplate = "https://randomuser.me/api";
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
