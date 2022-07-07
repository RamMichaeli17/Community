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
import restapi.webapp.entities.CellPhoneCompanyEntity;
import restapi.webapp.entities.UserEntity;
import restapi.webapp.exceptions.CompanyNotFoundException;
import restapi.webapp.exceptions.UserExistsException;
import restapi.webapp.exceptions.UserNotFoundException;
import restapi.webapp.exceptions.UsersNotFoundException;
import restapi.webapp.factories.UserDTOAssembler;
import restapi.webapp.factories.UserEntityAssembler;
import restapi.webapp.global.Utils;
import restapi.webapp.repos.CellPhoneCompanyRepo;
import restapi.webapp.repos.UserRepo;
import java.util.*;
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
    private final CellPhoneCompanyRepo cellPhoneCompanyRepo;

    @Autowired
    public UserService(UserRepo userRepo, UserEntityAssembler assembler, UserDTOAssembler userDTOAssembler,
                       CellPhoneCompanyRepo cellPhoneCompanyRepo) {
        this.userRepo = userRepo;
        this.assembler = assembler;
        this.userDTOAssembler = userDTOAssembler;
        this.cellPhoneCompanyRepo = cellPhoneCompanyRepo;

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
        List<UserEntity> userEntities = userRepo.findAll();
        userEntities.stream().findAny().orElseThrow(UsersNotFoundException::new);

        /* Explicitly declaring CollectionModel and not using getCorrespondingEntityType method because
        getAllUsers should return a CollectionModel in any case.
        */
        CollectionModel<EntityModel<UserEntity>> users = assembler.toCollectionModel(userEntities);
        return ResponseEntity.of(Optional.of(users));
    }

    /**
     * A method that deletes a user from the DB according to inputted user ID, if exists.
     * @param id ID of wanted user to be deleted
     * @return ResponseEntity of corresponding message.
     */
    public ResponseEntity<?> deleteUserById(@NonNull Long id) {
        userRepo.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        userRepo.deleteUserEntityByUserId(id);
        return ResponseEntity.ok("User with the ID: " + id + " has been deleted.");
    }

    /**
     * A method that deletes a user from the DB according to inputted user's email address, if exists.
     * @param email E-Mail address of wanted user to be deleted
     * @return ResponseEntity of corresponding message.
     */
    public ResponseEntity<?> deleteUserByEmail(@NonNull String email) {
        userRepo.getUserEntityByEmail(email).stream().findAny().orElseThrow(() -> new UserNotFoundException(email));
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
        // If user's email already exists, an exception will be thrown
        if(!userRepo.getUserEntityByEmail(user.getEmail()).isEmpty()){
            throw new UserExistsException(user.getEmail());
        }
        AvatarEntity avatarEntity = user.getAvatarEntity();
        if (avatarEntity.getEyes()>26 || avatarEntity.getEyes()<1)
            avatarEntity.setEyes(Utils.randomNumberBetweenMinAndMax(1,26));
        if (avatarEntity.getEyebrows()>10 || avatarEntity.getEyebrows()<1)
            avatarEntity.setEyebrows(Utils.randomNumberBetweenMinAndMax(1,10));
        if (avatarEntity.getMouth()>30 || avatarEntity.getEyes()<1)
            avatarEntity.setMouth(Utils.randomNumberBetweenMinAndMax(1,30));
        avatarEntity.setSeed(user.getEmail());
        avatarEntity.setResultUrl(avatarEntity.createResultUrl());
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
        // In case there's already a user with same credentials, changes won't be saved.
        userRepo.findById(user.getUserId()).orElseThrow(() ->
                new UserNotFoundException(user.getUserId()));

        // Making sure the seed is compatible with the user's email address and all fields are valid
        AvatarEntity avatarEntity = user.getAvatarEntity();
        if (avatarEntity.getEyes()>26 || avatarEntity.getEyes()<1)
            avatarEntity.setEyes(Utils.randomNumberBetweenMinAndMax(1,26));
        if (avatarEntity.getEyebrows()>10 || avatarEntity.getEyebrows()<1)
            avatarEntity.setEyebrows(Utils.randomNumberBetweenMinAndMax(1,10));
        if (avatarEntity.getMouth()>30 || avatarEntity.getEyes()<1)
            avatarEntity.setMouth(Utils.randomNumberBetweenMinAndMax(1,30));
        avatarEntity.setSeed(user.getEmail());
        avatarEntity.setResultUrl(avatarEntity.createResultUrl());
        user.setCellPhoneCompanies( userRepo.getUserEntityByUserId(user.getUserId()).get(0).getCellPhoneCompanies());

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
        users.stream().findAny().orElseThrow(() -> new UsersNotFoundException
                (String.format("City %s, Street %s %s, Country %s", city, streetName, streetNumber, country)));
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
        users.stream().findAny().orElseThrow(() -> new UserNotFoundException(String.format("%s %s", first, last)));
        return getCorrespondingEntityType(users);
    }

    /**
     * A method that fetches a user from the DB according to the requested parameter
     * and value that the user inputs, if exists.
     * @param param The requested parameter that the search is based on
     * @param value The requested value of the inputted parameter
     * @return ResponseEntity of the user, if exists.
     */
    public ResponseEntity<?> getUsersBySpecificParameter(@NonNull String param, @NonNull String value) {
        List<UserEntity> userEntities = this.methodsByParamsMap.get(param).apply(value);
        userEntities.stream().findAny().orElseThrow(() -> {
            switch (param) {
                case "id":
                case "email":
                    throw new UserNotFoundException(param, value);
                default:
                    throw new UsersNotFoundException(param, value);
            }
        });
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
    public ResponseEntity<?> getUsersByAgeAndName(@NonNull Integer lower, @NonNull Integer upper,
                                                  @NonNull String startingChar){
        List<UserEntity> userEntities = this.userRepo.getUserEntityByAgeBetweenAndLastNameStartingWith(lower,
                upper, startingChar);
        userEntities.stream().findAny().orElseThrow(UsersNotFoundException::new);
        return getCorrespondingEntityType(userEntities);
    }

    /**
     * A method that fetches a user from the DB by its user ID if exists, then convert it
     * into its DTO representation.
     * @param id ID of user to be fetched
     * @return ResponseEntity of the requested user, if exists.
     */
    public ResponseEntity<?> getUserDtoInfo(@NonNull Long id) {
        /*
        This method uses the pre-defined repo method findById because it returns an Optional<T>,
        which allows us to map the returned value into a UserDTO and return an exception easily,
        in case of an error.
         */

        return userRepo.findById(id)
                .map(UserDTO::new)
                .map(userDTOAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    /**
     * A method that fetches all the users on DB if they exist,
     * then convert them into their DTO representation.
     * @return ResponseEntity of all the users on DB, if they exist.
     */
    public ResponseEntity<?> getAllUsersDtoInfo() {
        List<UserEntity> userEntities = userRepo.findAll();
        // Check if there are any users that exist in DB
        userEntities.stream().findAny().orElseThrow(UsersNotFoundException::new);
        return ResponseEntity.ok(userDTOAssembler.toCollectionModel(userEntities
                        .stream()
                        .map(UserDTO::new)
                        .collect(Collectors.toList())));
    }

    /**
     * A method that gets a cell phone company ID, and returns all the user entities that are connected to it.
     * @param id Cell phone company's ID
     * @return ResponseEntity of all the corresponding users on DB, if they exist.
     */
    public ResponseEntity<?> getUserEntitiesByCellPhoneCompanyId(@NonNull Long id){
        List<UserEntity> users = userRepo.getUserEntitiesByCellPhoneCompanyId(id);
        // Check if there was any returned IDs of user entities, else throw exception
        users.stream().findAny().orElseThrow(UsersNotFoundException::new);

        return getCorrespondingEntityType(users);
    }

    public ResponseEntity<?> linkUserWithCellPhoneCompanies(@NonNull Long userId, @NonNull Set<@NonNull Long> companiesIds) {
        userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        UserEntity user = userRepo.getUserEntityByUserId(userId).get(0);
        Set<CellPhoneCompanyEntity> cellPhoneCompanyEntities = new HashSet<>();
        for (Long companyId : companiesIds) {
            cellPhoneCompanyRepo.findById(companyId).orElseThrow(() -> new CompanyNotFoundException(companyId));
            cellPhoneCompanyEntities.add(cellPhoneCompanyRepo.getCellPhoneCompanyByCellPhoneCompanyId(companyId));
        }
        user.getCellPhoneCompanies().addAll(cellPhoneCompanyEntities);
        userRepo.save(user);
        log.info("User {} has been linked with the cell phone companies above: {}", userId,companiesIds);
        return ResponseEntity.of(Optional.of(assembler.toModel(user)));
    }
}