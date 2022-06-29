package restapi.webapp.services;

import restapi.webapp.dtos.UserDTO;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import restapi.webapp.entities.AvatarEntity;
import restapi.webapp.entities.UserEntity;
import restapi.webapp.factories.UserDTOAssembler;
import restapi.webapp.factories.UserEntityAssembler;
import restapi.webapp.repos.UserRepo;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class UserService {
    private final UserRepo userRepo;
    private final UserEntityAssembler assembler;
    private final UserDTOAssembler userDTOAssembler;
    private final HashMap<String, Function<String, List<UserEntity>>> methodsByParamsMap;

    @Autowired
    public UserService(UserRepo userRepo, UserEntityAssembler assembler, UserDTOAssembler userDTOAssembler) {
        this.userRepo = userRepo;
        this.assembler = assembler;
        this.userDTOAssembler = userDTOAssembler;

        this.methodsByParamsMap = new HashMap<>();
        this.methodsByParamsMap.put("id", id -> userRepo.getUserEntityByUserId(Long.valueOf(id)));
        this.methodsByParamsMap.put("phone", userRepo::getUserEntityByPhoneNumbers);
        this.methodsByParamsMap.put("phoneContains", userRepo::getUserEntityByPhoneNumbersContains);
        this.methodsByParamsMap.put("age", age -> userRepo.getUserEntitiesByAge(Integer.valueOf(age)));
        this.methodsByParamsMap.put("gender", userRepo::getUserEntitiesByGender);
        this.methodsByParamsMap.put("email", userRepo::getUserEntityByEmail);
    }

    private ResponseEntity<? extends RepresentationModel<? extends RepresentationModel<?>>> returnEntityList
            (List<UserEntity> userEntities) {
        if (userEntities.size() == 1) {
            UserEntity userEntity = userEntities.get(0);
            EntityModel<UserEntity> userEntityModel = assembler.toModel(userEntity);
            return ResponseEntity.of(Optional.of(userEntityModel));
        }
        CollectionModel<EntityModel<UserEntity>> userEntitiesModel = assembler.toCollectionModel(userEntities);
        return ResponseEntity.of(Optional.of(userEntitiesModel));
    }

    public ResponseEntity<?> getAllUsers(){
        List<UserEntity> users = userRepo.findAll();
        return returnEntityList(users);
    }

    public ResponseEntity<?> deleteUserById(@NonNull Long id) {
        userRepo.deleteUserEntityByUserId(id);
        return ResponseEntity.ok("User with the ID: " + id + " has been deleted.");
    }

    public ResponseEntity<?> deleteUserByEmail(@NonNull String email) {
        userRepo.deleteUserEntityByEmail(email);
        return ResponseEntity.ok("User with the email: " + email + " has been deleted.");
    }

    public ResponseEntity<?> createUser(@NonNull UserEntity user){
        AvatarEntity currentAvatarEntity = user.getAvatarEntity();
        currentAvatarEntity.setSeed(user.getEmail());
        currentAvatarEntity.setResultUrl(currentAvatarEntity.createResultUrl());
        user.setAvatarEntity(currentAvatarEntity);

        userRepo.save(user);
        log.info("User {} has been created", user.getUserId());
        return ResponseEntity.of(Optional.of(assembler.toModel(user)));
    }

    public ResponseEntity<?> updateUser(@NonNull UserEntity user) {
        // In case there's already a user with same credentials, it will save the changes.
        userRepo.save(user);
        log.info("User {} has been updated", user.getUserId());
        return ResponseEntity.of(Optional.of(assembler.toModel(user)));
    }

    public ResponseEntity<?> getUsersByLocation(@NonNull String city, @NonNull String streetName,
                                                @NonNull String streetNumber, @NonNull String country){
        List<UserEntity> users = userRepo.getUserEntitiesByLocation(city, streetName, streetNumber, country);
        return returnEntityList(users);
    }

    public ResponseEntity<?> getUsersByName(@NonNull String first, @NonNull String last){
        List<UserEntity> users = userRepo.getUserEntitiesByName(first, last);
        return returnEntityList(users);
    }

    public ResponseEntity<?> getUserBySpecificParameter(@NonNull String param, @NonNull String value) {
        List<UserEntity> userEntities = this.methodsByParamsMap.get(param).apply(value);
        return returnEntityList(userEntities);
    }

    public ResponseEntity<?> getUserByAgeAndName(@NonNull Integer lower, @NonNull Integer upper, @NonNull String startingChar){
        List<UserEntity> userEntities = this.userRepo.getUserEntityByAgeBetweenAndLastNameStartingWith(lower,
                upper, startingChar);
        return returnEntityList(userEntities);
    }

    public ResponseEntity<?> getUserDtoInfo(@NonNull Long id) {
        return userRepo.findById(id)
                .map(UserDTO::new)
                .map(userDTOAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> getAllUsersDtoInfo() {
        return ResponseEntity.ok(userDTOAssembler.toCollectionModel(
                userRepo.findAll().stream()
                        .map(UserDTO::new)
                        .collect(Collectors.toList())));
    }
}