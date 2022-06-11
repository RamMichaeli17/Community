package restapi.webapp.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restapi.webapp.entities.UserEntity.*;
import restapi.webapp.entities.UserEntity;
import restapi.webapp.enums.AvatarGroups;
import restapi.webapp.repos.UserRepo;

import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    /*public UserEntity getUserByName(String firstName, String lastName){
        return userRepo.getUserEntityByName(firstName, lastName);
    }*/

    public UserEntity getUserByEmail(String email){
        return userRepo.getUserEntityByEmail(email);
    }

    public List<UserEntity> getAllUsers(){
        return userRepo.findAll();
    }

    public List<UserEntity> getUsersByAge(Integer age){
        return userRepo.getUserEntitiesByAge(age);
    }
}