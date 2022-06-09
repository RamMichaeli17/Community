package restapi.webapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import restapi.webapp.repos.UserRepo;

@Configuration
public class SeedDB {


    @Bean
    CommandLineRunner initDatabase(UserRepo userRepo) {
        return args -> {
//            Name name1 = new Name("Mr","tal","beno");
//            Name name2 = new Name("Mr","tal","beno");
//
//
//            User user1 = userRepo.save(new User("beno@gmail.com","4897998","male",26,"0505878952",name1));
//            User user2 = userRepo.save(new User());
//
//            userRepo.save(user1);
//            userRepo.save(user2);
        };
    }
}
