package restapi.webapp.controllers;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import restapi.webapp.dtos.UserDTO;
import restapi.webapp.entities.UserEntity;
import restapi.webapp.services.UserService;
import java.util.Set;

/**
 * A class that represents the controller of the UserEntity,
 * containing various endpoints for getting information about users,
 * searching users by specific parameters or by combination of parameters, editing user's information,
 * as well as creation and deletion of users.
 */
@RestController
@RequestMapping("/users")
@Slf4j
@Tag(name = "User Controller", description = "The controller of User entity")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * A method that returns all the users that exist in the DB, if any.
     * @return ResponseEntity of the returned users.
     */
    @GetMapping("/getAllUsers")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Find all users",
            description = "Find user details by name, location age and more",
            tags = {"User Controller"})
    public ResponseEntity<CollectionModel<EntityModel<UserEntity>>> getAllUsers() {
        log.info("Trying to fetch all users");
        ResponseEntity<CollectionModel<EntityModel<UserEntity>>> response = this.userService.getAllUsers();
        log.info("{}", response);
        return response;
    }

    /**
     * A method that fetches a user according to the requested parameter
     * and value that the user inputs, if exists.
     * @param param The requested parameter that the search is based on.
     * @param value The requested value of the inputted parameter.
     * @return ResponseEntity of the user, if exists.
     */
    @GetMapping("/get/{param}/{value}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Find a user by specific parameters By Path Variable",
            description = "Find user details by name, location, age and more",
            tags = {"User Controller"})
    public ResponseEntity<?> getUsersWithPathVar(@PathVariable String param, @PathVariable String value) {
        log.info("Trying to get users by param \"{}\" and value \"{}\"", param, value);
        ResponseEntity<?> response = this.userService.getUsersBySpecificParameter(param, value);
        log.info("{}", response);
        return response;
    }

    /**
     * A method that creates a UserEntity based on the content that is given in the request body.
     * @param user User to be created.
     * @return ResponseEntity of the created user.
     */
    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a user",
            description = "Create a new user by specific parameters",
            responses = {@ApiResponse(responseCode = "201", description = "User created")},
            tags = {"User Controller"})
    public ResponseEntity<EntityModel<UserEntity>> createUser(@RequestBody UserEntity user) {
        log.info("Trying to create a new user by specific parameters:");
        log.info("{}", user);
        return this.userService.createUser(user);
    }

    /**
     * A method that updates a UserEntity based on the content that is given in the request body, if exists.
     * @param user User to be updated.
     * @return ResponseEntity of the updated user, if exists.
     */
    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update a user",
            description = "Update a specific user",
            tags = {"User Controller"})
    public ResponseEntity<EntityModel<UserEntity>> updateUser(@RequestBody UserEntity user) {
        log.info("Trying to update user by specific parameters");
        log.info("{}", user);
        return this.userService.updateUser(user);
    }

    /**
     * A method that deletes a user according to the inputted user ID given, if exists.
     * @param userId User ID to be deleted.
     * @return ResponseEntity that contains the corresponding message for the deletion request.
     */
    @Transactional
    @DeleteMapping("/deleteByUserId")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete a user by their ID",
            description = "Delete a specific user by their ID",
            tags = {"User Controller"})
    public ResponseEntity<?> deleteUserById(@RequestParam Long userId) {
        log.info("Trying to delete user with ID: {}", userId);
        ResponseEntity<?> response = this.userService.deleteUserById(userId);
        log.info("{}", response);
        return response;
    }

    /**
     * A method that deletes a user according to the inputted email given, if exists.
     * @param email E-Mail address that belongs to the user to be deleted.
     * @return ResponseEntity that contains the corresponding message for the deletion request.
     */
    @Transactional
    @DeleteMapping("/deleteByEmail")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete a user by E-Mail address",
            description = "Delete a specific user by their E-Mail address",
            tags = {"User Controller"})
    public ResponseEntity<?> deleteUserByEmail(@RequestParam String email) {
        log.info("Trying to delete user with email: {}", email);
        ResponseEntity<?> response = this.userService.deleteUserByEmail(email);
        log.info("{}", response);
        return response;
    }

    /**
     * A method that gets a specific user by first name and last name, if exists.
     * @param firstName First name of the wanted user.
     * @param lastName Last name of the wanted user.
     * @return ResponseEntity of the requested user, if exists.
     */
    @GetMapping("/get/name")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Find a specific user by name",
            description = "Delete a specific user by their first name and last name",
            tags = {"User Controller"})
    public ResponseEntity<?> getUserByName(@RequestParam("first") String firstName,
                                           @RequestParam("last") String lastName){
        log.info("Trying to fetch users by name: {} {}", firstName, lastName);
        ResponseEntity<?> response = this.userService.getUsersByName(firstName, lastName);
        log.info("{}", response);
        return response;
    }

    /**
     * A method that gets a specific user by specific location, if exists.
     * @param city City of the user to be fetched.
     * @param streetName Street name of the user to be fetched.
     * @param streetNumber Street number of the user to be fetched.
     * @param country Country of the user to be fetched.
     * @return ResponseEntity of the requested user, if exists.
     */
    @GetMapping("/get/location")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Find a specific user by location",
            description = "Delete a specific user by their city, street name, street number and country",
            tags = {"User Controller"})
    public ResponseEntity<?> getUsersByLocation(@RequestParam String city,
                                                @RequestParam String streetName,
                                                @RequestParam String streetNumber,
                                                @RequestParam String country) {
        log.info("Trying to fetch users by location: {}, {}, {},{}", city, streetName, streetNumber, country);
        ResponseEntity<?> response = this.userService.getUsersByLocation(city, streetName, streetNumber, country);
        log.info("{}", response);
        return response;
    }

    /**
     * A method that fetches a specific user by lower and upper range of age,
     * and the first letter of the requested user's last name.
     * @param lower Lower bound of user's age to be fetched.
     * @param upper Upper bound of user's age to be fetched.
     * @param startingChar Starting character of user's last name to be fetched.
     * @return ResponseEntity of the requested user, if exists.
     */
    @GetMapping("/get/userByAgeAndLastName")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Find a specific user by their age and last name",
            description = "Find a specific user by their age and the first digit on their last name",
            tags = {"User Controller"})
    public ResponseEntity<?> getUsersByAgeAndLastName(@RequestParam Integer lower,
                                                @RequestParam Integer upper,
                                                @RequestParam String startingChar) {
        log.info("Trying to fetch users by advanced querying: Lower bound: {}, Upper bound: {}, " +
                "Starting char at last name: {}", lower, upper, startingChar);
        ResponseEntity<?> response = this.userService.getUsersByAgeAndName(lower, upper, startingChar);
        log.info("{}", response);
        return response;
    }

    /**
     * A method that gets a user's DTO representation based on its user ID, if exists.
     * @param id ID of user to be fetched.
     * @return ResponseEntity of the requested user, if exists.
     */
    @GetMapping("/getUser/{id}/info")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get only social information about a specific user",
            description = "Get a userDTO that contains only some information about a user we want to present",
            tags = {"User Controller"})
    public ResponseEntity<EntityModel<UserDTO>> getUserDtoInfo(@PathVariable Long id) {
        log.info("Trying to get user info by ID: " + id);
        ResponseEntity<EntityModel<UserDTO>> response = this.userService.getUserDtoInfo(id);
        log.info("{}", response);
        return response;
    }

    /**
     * A method that gets the DTO representation of the users that exist on DB, if they exist.
     * @return ResponseEntity of the requested users, if they exist.
     */
    @GetMapping("/getAllUsers/info")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all the social information about the users",
            description = "Get all userDTOs that contain partial information we present about users",
            tags = {"User Controller"})
    public ResponseEntity<CollectionModel<EntityModel<UserDTO>>> getAllUsersDtoInfo() {
        log.info("Trying to get all users info");
        ResponseEntity<CollectionModel<EntityModel<UserDTO>>> response = this.userService.getAllUsersDtoInfo();
        log.info("{}", response);
        return response;
    }

    /**
     * A method that gets a cell phone company ID, and returns all the user entities that are connected to it.
     * @param id Cell phone company's ID
     * @return ResponseEntity of all the corresponding users on DB, if they exist.
     */
    @GetMapping("get/companyId/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get user entities by cell phone company's ID",
            description = "Get user entities that are connected to a specific cell phone company ID",
            tags = {"User Controller"})
    public ResponseEntity<?> getUserEntitiesByCompanyId(@PathVariable Long id){
        log.info("Trying to get user entities by company ID: {}", id);
        ResponseEntity<?> response = this.userService.getUserEntitiesByCellPhoneCompanyId(id);
        log.info("{}", response);
        return response;
    }

    @PutMapping("/linkUserWithCompanies")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update a user",
            description = "Update a specific user",
            tags = {"User Controller"})
    public ResponseEntity<EntityModel<UserDTO>> linkUserWithCellPhoneCompanies(@RequestParam Long userId,
                                                            @RequestParam Set<Long> companiesIds) {
        log.info("Trying to link user {} with the cell phone companies above: {}", userId, companiesIds);
        ResponseEntity<EntityModel<UserDTO>> response = this.userService
                .linkUserWithCellPhoneCompanies(userId,companiesIds);
        log.info("{}", response);
        return response;
    }
}