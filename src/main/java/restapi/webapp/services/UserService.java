package restapi.webapp.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import restapi.webapp.entities.UserEntity;
import restapi.webapp.factories.UserEntityAssembler;
import restapi.webapp.repos.UserRepo;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;

@Service
@Slf4j
public class UserService {
    private final UserRepo userRepo;
    private final UserEntityAssembler assembler;
    private final HashMap<String, Function<String, ResponseEntity<?>>> methodsByParamsMap;

    @Autowired
    public UserService(UserRepo userRepo, UserEntityAssembler assembler) {
        this.userRepo = userRepo;
        this.assembler = assembler;

        this.methodsByParamsMap = new HashMap<>();
        this.methodsByParamsMap.put("phone", phone -> getUserByPhone(phone));
        this.methodsByParamsMap.put("age", age -> getUsersByAge(Integer.valueOf(age)));
        this.methodsByParamsMap.put("gender", gender -> getUsersByGender(gender));
        //this.methodsByParamsMap.put("location")
        //this.methodsByParamsMap.put("name")
    }

    public ResponseEntity<?> getUserByEmail(String email){
        EntityModel<UserEntity> user = assembler.toModel(userRepo.getUserEntityByEmail(email));
        return ResponseEntity.of(Optional.of(user));
    }

    public ResponseEntity<?> getAllUsers(){
        CollectionModel<EntityModel<UserEntity>> users = assembler.toCollectionModel(userRepo.findAll());
        return ResponseEntity.of(Optional.of(users));
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
        log.info("User {} has been updated", user.getEmail());
        return ResponseEntity.of(Optional.of(assembler.toModel(user)));
    }

    public ResponseEntity<?> getUserByName(String first, String last){
        EntityModel<UserEntity> user = assembler.toModel(userRepo.getUserEntityByName(first, last));
        return ResponseEntity.of(Optional.of(user));
    }

    public ResponseEntity<?> getUsersByLocation(String city, String street, String country){
        CollectionModel<EntityModel<UserEntity>> users = assembler.toCollectionModel(
                userRepo.getUserEntitiesByLocation(city, street, country));
        return ResponseEntity.of(Optional.of(users));
    }

    public ResponseEntity<?> getUsersByGender(String gender){
        CollectionModel<EntityModel<UserEntity>> users = assembler.toCollectionModel(
                userRepo.getUserEntitiesByGender(gender));
        return ResponseEntity.of(Optional.of(users));
    }

    public ResponseEntity<?> getUsersByAge(Integer age){
        CollectionModel<EntityModel<UserEntity>> users = assembler.toCollectionModel(
                userRepo.getUserEntitiesByAge(age));
        return ResponseEntity.of(Optional.of(users));
    }

    public ResponseEntity<?> getUserByPhone(String phone){
        EntityModel<UserEntity> user = assembler.toModel(userRepo.getUserEntityByPhone(phone));
        return ResponseEntity.of(Optional.of(user));
    }

    public ResponseEntity<?> getUserBySpecificParameter(@NonNull String param, @NonNull String value) {
        Function<String, ResponseEntity<?>> function = methodsByParamsMap.get(param);
        return function.apply(value);
    }
}