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

//todo: Change ResponseEntity<?

/**
 * A class that operates as the service of the user entity, containing the business logic of
 * the operations that are given to the user.
 */
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

        // Populate HashMap of methods. Specific method will be injected on runtime.
        this.methodsByParamsMap = new HashMap<>();
        this.methodsByParamsMap.put("id", id -> userRepo.getUserEntityByUserId(Long.valueOf(id)));
        this.methodsByParamsMap.put("phone", userRepo::getUserEntityByPhoneNumbers);
        this.methodsByParamsMap.put("phoneContains", userRepo::getUserEntityByPhoneNumbersContains);
        this.methodsByParamsMap.put("age", age -> userRepo.getUserEntitiesByAge(Integer.valueOf(age)));
        this.methodsByParamsMap.put("gender", userRepo::getUserEntitiesByGender);
        this.methodsByParamsMap.put("email", userRepo::getUserEntityByEmail);
    }

    /**
     * A method that gets a list of user entities, and converts the entities into an
     * EntityModel or a CollectionModel, according to the size of the list,
     * then putting the object within a ResponseEntity
     * @param userEntities List of user entities to be checked
     * @return ResponseEntity of the corresponding type of users
     */
    private ResponseEntity<? extends RepresentationModel<? extends RepresentationModel<?>>> getCorrespondingEntityType
            (List<UserEntity> userEntities) {
        if (userEntities.size() == 1) {
            UserEntity userEntity = userEntities.get(0);
            EntityModel<UserEntity> userEntityModel = assembler.toModel(userEntity);
            return ResponseEntity.of(Optional.of(userEntityModel));
        }
        CollectionModel<EntityModel<UserEntity>> userEntitiesModel = assembler.toCollectionModel(userEntities);
        return ResponseEntity.of(Optional.of(userEntitiesModel));
    }

    /**
     * A method that fetches all the users that the DB contains, if any.
     * @return ResponseEntity of returned users.
     */
    public ResponseEntity<?> getAllUsers(){
        CollectionModel<EntityModel<UserEntity>> users = assembler.toCollectionModel(userRepo.findAll());
        return ResponseEntity.of(Optional.of(users));
    }

    /**
     * A method that deletes a user from the DB according to inputted user ID, if exists.
     * @param id ID of wanted user to be deleted
     * @return ResponseEntity of corresponding message.
     */
    public ResponseEntity<?> deleteUserById(@NonNull Long id) {
        userRepo.deleteUserEntityByUserId(id);
        return ResponseEntity.ok("User with the ID: " + id + " has been deleted.");
    }

    /**
     * A method that deletes a user from the DB according to inputted user's email address, if exists.
     * @param email E-Mail address of wanted user to be deleted
     * @return ResponseEntity of corresponding message.
     */
    public ResponseEntity<?> deleteUserByEmail(@NonNull String email) {
        userRepo.deleteUserEntityByEmail(email);
        return ResponseEntity.ok("User with the email: " + email + " has been deleted.");
    }

    /**
     * A method that gets a user entity as a parameter and completes its missing values,
     * then saving it into the DB.
     * @param user User entity to be inserted into the DB
     * @return ResponseEntity of the created user
     */
    public ResponseEntity<?> createUser(@NonNull UserEntity user){
        AvatarEntity currentAvatarEntity = user.getAvatarEntity();
        currentAvatarEntity.setSeed(user.getEmail());
        currentAvatarEntity.setResultUrl(currentAvatarEntity.createResultUrl());
        user.setAvatarEntity(currentAvatarEntity);

        userRepo.save(user);
        log.info("User {} has been created", user.getUserId());
        return ResponseEntity.of(Optional.of(assembler.toModel(user)));
    }

    /**
     * A method that gets a user entity as a parameter, updates the corresponding fields and saves the user
     * into the DB.
     * @param user User entity to be updated.
     * @return ResponseEntity of the updated user.
     */
    public ResponseEntity<?> updateUser(@NonNull UserEntity user) {
        // In case there's already a user with same credentials, it will save the changes.
        userRepo.save(user);
        log.info("User {} has been updated", user.getUserId());
        return ResponseEntity.of(Optional.of(assembler.toModel(user)));
    }

    /**
     * A method that fetches a specific user on DB by specific location, if exists.
     * @param city City to be checked.
     * @param streetName Street name to be checked.
     * @param streetNumber Street number to be checked.
     * @param country Country to be checked.
     * @return ResponseEntity of the requested user, if exists.
     */
    public ResponseEntity<?> getUsersByLocation(@NonNull String city, @NonNull String streetName,
                                                @NonNull String streetNumber, @NonNull String country){
        List<UserEntity> users = userRepo.getUserEntitiesByLocation(city, streetName, streetNumber, country);
        return getCorrespondingEntityType(users);
    }

    /**
     * A method that fetches a specific user on DB by specific first and last name, if exists.
     * @param first First name to be checked.
     * @param last Last name to be checked.
     * @return ResponseEntity of the requested user, if exists.
     */
    public ResponseEntity<?> getUsersByName(@NonNull String first, @NonNull String last){
        List<UserEntity> users = userRepo.getUserEntitiesByName(first, last);
        return getCorrespondingEntityType(users);
    }

    /**
     * A method that fetches a user from the DB according to the requested parameter
     * and value that the user inputs, if exists.
     * @param param The requested parameter that the search is based on
     * @param value The requested value of the inputted parameter
     * @return ResponseEntity of the user, if exists.
     */
    public ResponseEntity<?> getUserBySpecificParameter(@NonNull String param, @NonNull String value) {
        List<UserEntity> userEntities = this.methodsByParamsMap.get(param).apply(value);
        return getCorrespondingEntityType(userEntities);
    }

    /**
     * A method that fetches a specific user from the DB by lower and upper range of age,
     * and the first letter of the requested user's last name
     * @param lower Lower bound of user's age to be checked.
     * @param upper Upper bound of user's age to be checked.
     * @param startingChar Starting character of user's last name to be checked.
     * @return ResponseEntity of the requested user, if exists.
     */
    public ResponseEntity<?> getUserByAgeAndName(@NonNull Integer lower, @NonNull Integer upper, @NonNull String startingChar){
        List<UserEntity> userEntities = this.userRepo.getUserEntityByAgeBetweenAndLastNameStartingWith(lower,
                upper, startingChar);
        return getCorrespondingEntityType(userEntities);
    }

    /**
     * A method that fetches a user from the DB by its user ID if exists, then convert it
     * into its DTO representation.
     * @param id ID of user to be fetched
     * @return ResponseEntity of the requested user, if exists.
     */
    public ResponseEntity<?> getUserDtoInfo(@NonNull Long id) {
        return userRepo.findById(id)
                .map(UserDTO::new)
                .map(userDTOAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * A method that fetches all the users on DB if they exist,
     * then convert them into their DTO representation.
     * @return ResponseEntity of all the users on DB, if they exist.
     */
    public ResponseEntity<?> getAllUsersDtoInfo() {
        return ResponseEntity.ok(userDTOAssembler.toCollectionModel(
                userRepo.findAll().stream()
                        .map(UserDTO::new)
                        .collect(Collectors.toList())));
    }
}