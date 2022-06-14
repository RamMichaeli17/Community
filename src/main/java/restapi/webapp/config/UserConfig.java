package restapi.webapp.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import restapi.webapp.entities.UserEntity.*;
import restapi.webapp.entities.UserEntity;
import restapi.webapp.enums.AvatarGroups;
import restapi.webapp.repos.UserRepo;

@Configuration
@Slf4j
@EnableRetry
//@EnableScheduling

public class UserConfig {
    @Bean
    CommandLineRunner initDatabase(UserRepo userRepo) {
        return args -> {
            log.info("Creating Users...");

            UserEntity user1 = userRepo.save(new UserEntity("tal.beno@nice.com", "202cb962ac59075b964b07152d234b70", "male", 27,"0542070875",
                    new Name("Mr","Tal","Beno"),
                    new Location("Rehovot", "Harav Shauli", "Israel", 66209),
                    new Avatar(AvatarGroups.ADVENTURER, true)));
            log.info("User1 has initiated: " + user1);

            UserEntity user2 = userRepo.save(new UserEntity("yaniv@gmail.com", "202cnz2a215964b07137d31D614b23", "male", 25, "0504340408",
                    new Name("Mr","Yaniv","Levi"),
                    new Location("Dummy city", "Dummy Street", "Israel", 123456),
                    new Avatar(AvatarGroups.BIG_SMILE, false)));
            log.info("User2 has been created." + user2);

            UserEntity user3 = userRepo.save(new UserEntity("ram@gmail.com", "202zxzxcccb962a789912964bd2344b70", "male", 24,"0582147625",
                    new Name("Mr","Ram","Michaeli"),
                    new Location("Ashdod", "Arik Einstein", "Israel", 7778542),
                    new Avatar(AvatarGroups.PIXEL_ART, true)));
            log.info("User3 has been created." + user3);

            log.info("All users have been initiated");
        };
    }

}