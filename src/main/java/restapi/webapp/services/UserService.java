package restapi.webapp.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restapi.webapp.entities.UserEntity.*;
import restapi.webapp.entities.UserEntity;
import restapi.webapp.repos.UserRepo;

@Service
@Slf4j
public class UserService {

    private UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public String createUser() {
        log.info("Creating User");
//        Name name1 = new Name("Mr","tal","beno");
//        Name name2 = new Name("Mr","tal","beno");
//
//
//        User user1 = userRepo.save(new User("beno@gmail.com","4897998","male",26,"0505878952",name1));
//        User user2 = userRepo.save(new User());
//
//        userRepo.save(user1);
//        userRepo.save(user2);
        return "User created";
    }
}