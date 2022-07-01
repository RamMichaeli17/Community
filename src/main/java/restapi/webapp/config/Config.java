package restapi.webapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import restapi.webapp.entities.AvatarEntity;
import restapi.webapp.entities.CellPhoneCompanyEntity;
import restapi.webapp.entities.UserEntity.*;
import restapi.webapp.entities.UserEntity.Location.*;
import restapi.webapp.entities.UserEntity;
import restapi.webapp.enums.HairColor;
import restapi.webapp.repos.CellPhoneCompanyRepo;
import restapi.webapp.repos.UserRepo;

import java.util.Set;

@Configuration
@Slf4j
@EnableRetry

public class Config {
    @Bean
    CommandLineRunner initDatabase(CellPhoneCompanyRepo cellPhoneCompanyRepo, UserRepo userRepo) {
        return args -> {

            log.info("Creating Cell Phone Companies...");

            CellPhoneCompanyEntity cellcom = cellPhoneCompanyRepo.save(new CellPhoneCompanyEntity(
                    "Cellcom",Set.of()
            ));
            log.info("Cellcom has been created: " + cellcom);

            CellPhoneCompanyEntity orange = cellPhoneCompanyRepo.save(new CellPhoneCompanyEntity(
                    "Orange", Set.of("Argentina","Brazil","Israel")
            ));
            log.info("Orange has been created: " + orange);

            CellPhoneCompanyEntity partner =
                    cellPhoneCompanyRepo.save(new CellPhoneCompanyEntity("Partner", Set.of("Spain","Turkey")));
            log.info("Partner has been created: " + partner);

            log.info("All cell phone companies have been created");


            log.info("Creating Users...");

            UserEntity user1 = userRepo.save(new UserEntity("tal.beno@nice.com",
                    "202cb962ac59075b964b07152d234b70", "male", 25, Set.of("0542070875","0544890875"),
                    new Name("Mr","Tal","Beno"),
                    new Location("Rehovot", new Street("Harav Shauli", "1"), "Israel"),
                    new AvatarEntity(7,4, HairColor.red01,27)));
            log.info("User1 has initiated: " + user1);

            UserEntity user2 = userRepo.save(new UserEntity("yaniv@gmail.com",
                    "202cnz2a215964b07137d31D614b23", "male", 25, Set.of("0504340408"),
                    new Name("Mr","Yaniv","Levi"),
                    new Location("Dummy city", new Street("Dummy Street", "3"), "Israel"),
                    new AvatarEntity(4,2, HairColor.brown01,2)));
            log.info("User2 has been created." + user2);

            UserEntity user3 = userRepo.save(new UserEntity("ram@gmail.com",
                    "202zxzxcccb962a789912964bd2344b70", "male", 24,Set.of("0582147625"),
                    new Name("Mr","Ram","Michaeli"),
                    new Location("Ashdod", new Street("Arik Miller","56"), "Israel"),
                    new AvatarEntity(5,9, HairColor.blonde01,17)));
            log.info("User3 has been created." + user3);

            log.info("All users have been initiated");
        };
    }

    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

}