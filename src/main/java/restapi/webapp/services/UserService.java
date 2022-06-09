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

}