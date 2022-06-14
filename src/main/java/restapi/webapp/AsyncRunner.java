package restapi.webapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import restapi.webapp.entities.UserEntity;
import restapi.webapp.services.ApiService;

import java.util.concurrent.CompletableFuture;

@Component
public class AsyncRunner implements CommandLineRunner {
    private final ApiService apiService;
    private static final Logger classLogger = LoggerFactory.getLogger(AsyncRunner.class);

    public AsyncRunner(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void run(String... args) throws Exception {
        CompletableFuture<UserEntity> user1 = apiService.getUserByType("male");
        CompletableFuture<UserEntity> user2 = apiService.getUserByType("female");
        CompletableFuture<UserEntity> user3 = apiService.getUserByType("random");

        CompletableFuture<UserEntity>[] taskArray = new CompletableFuture[3];
        taskArray[0] = user1;
        taskArray[1] = user2;
        taskArray[2] = user3;

        CompletableFuture.allOf(taskArray);
        /*
        allOf - executes multiple CompletableFuture objects in parallel
        join - return the result values when complete opr throw an unchecked exception
         */
        CompletableFuture.allOf(user1,user2,user3).join();

        classLogger.info("User1 = " + user1.get());
        classLogger.info("User2 = " + user2.get());
        classLogger.info("User3 = " + user3.get());


    }
}
