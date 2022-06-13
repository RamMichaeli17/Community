package restapi.webapp.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import restapi.webapp.entities.UserEntity;
import restapi.webapp.factories.UserEntityAssembler;
import restapi.webapp.repos.UserRepo;

import java.util.Optional;

@Service
@Slf4j
public class UserService {
    private final UserRepo userRepo;
    private final UserEntityAssembler assembler;

    @Autowired
    public UserService(UserRepo userRepo, UserEntityAssembler assembler) {
        this.userRepo = userRepo;
        this.assembler = assembler;
    }

    public ResponseEntity<?> getUserByEmail(String email){
        UserEntity user = userRepo.getUserEntityByEmail(email);
        return ResponseEntity.of(Optional.of(assembler.toModel(user)));
    }

    public ResponseEntity<?> getAllUsers(){
        return ResponseEntity.of(Optional.of(assembler.toCollectionModel(userRepo.findAll())));
    }

    public ResponseEntity<?> deleteUser(String id) {
        userRepo.deleteUserEntityByEmail(id);
        return ResponseEntity.ok("User " + id + " has been deleted.");
    }

    public ResponseEntity<?> createUser(UserEntity user){
        userRepo.save(user);
        log.info("User {} has been created", user.getEmail());
        return ResponseEntity.of(Optional.of(assembler.toModel(user)));
    }

    public ResponseEntity<?> updateUser(UserEntity user) {
        // In case there's already a user with same credentials, it will save the changes.
        userRepo.save(user);
        return ResponseEntity.of(Optional.of(user));
    }

    public ResponseEntity<?> getUserByName(String first, String last){
        return ResponseEntity.of(Optional.
                of(assembler.toModel(userRepo.getUserEntityByName(first, last))));
    }

    public ResponseEntity<?> getUsersByGender(String gender){
        return ResponseEntity.of(Optional.of
                (assembler.toCollectionModel(userRepo.getUserEntitiesByGender(gender))));
    }

    public ResponseEntity<?> getUsersByAge(Integer age){
        return ResponseEntity.of(Optional
                .of(assembler.toCollectionModel(userRepo.getUserEntitiesByAge(age))));
    }

    public ResponseEntity<?> getUserByPhone(String phone){
        return ResponseEntity.of(Optional
                .of(assembler.toModel(userRepo.getUserEntityByPhone(phone))));
    }

    public ResponseEntity<?> getUserByLocation(String street, String city, String state){
        return ResponseEntity.of(Optional.
                of(assembler.toCollectionModel(userRepo.getUserEntitiesByLocation(street, city, state))));
    }

    /*public ResponseEntity<?> getUserBySpecificParameter(@NonNull String param, @NonNull String value) {
        UserEntity user = userRepo.getUserEntityBy(param, value);
        return ResponseEntity.of(Optional.of(user));
    }*/
}