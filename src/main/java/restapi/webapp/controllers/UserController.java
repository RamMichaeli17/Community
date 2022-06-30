package restapi.webapp.controllers;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import restapi.webapp.entities.UserEntity;
import restapi.webapp.factories.UserDTOAssembler;
import restapi.webapp.repos.UserRepo;
import restapi.webapp.services.UserService;

@RestController
@RequestMapping("/control")
@Slf4j
@Tag(name = "User Controller", description = "The controller of User entity")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getAllUsers")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Find all users",
            description = "Find user details by name, location age and more",
            tags = {"User Controller"})
    public ResponseEntity<?> getAllUsers() {
        log.info("Trying to fetch all users");
        return this.userService.getAllUsers();
    }

    @GetMapping("/get/{param}/{value}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Find a user by specific parameters By Path Variable",
            description = "Find user details by name, location, age and more",
            tags = {"User Controller"})
    public ResponseEntity<?> getUserWithPathVar(@PathVariable String param, @PathVariable String value) {
        log.info("Trying to get user by param \"{}\" and value \"{}\"", param, value);
        return this.userService.getUserBySpecificParameter(param, value);
    }

    //todo: handle the situation where you press create with the same body
    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a user",
            description = "Create a new user by specific parameters",
            responses = {@ApiResponse(responseCode = "201", description = "User created")},
            tags = {"User Controller"})
    public ResponseEntity<?> createUser(@RequestBody UserEntity user) {
        log.info("Trying to create a new user by specific parameters:");
        log.info("{}", user);
        return this.userService.createUser(user);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update a user",
            description = "Update a specific user",
            tags = {"User Controller"})
    public ResponseEntity<?> updateUser(@RequestBody UserEntity user) {
        log.info("Trying to update user by specific parameters");
        log.info("{}", user);
        return this.userService.updateUser(user);
    }

    //todo: maybe merge with deleteByEmail if we want
    @Transactional
    @DeleteMapping("/deleteByUserId")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete a user by their ID",
            description = "Delete a specific user by their ID",
            tags = {"User Controller"})
    public ResponseEntity<?> deleteUserById(@RequestParam Long userId) {
        log.info("Trying to delete user with ID: {}", userId);
        return this.userService.deleteUserById(userId);
    }

    @Transactional
    @DeleteMapping("/deleteByEmail")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete a user by E-Mail address",
            description = "Delete a specific user by their E-Mail address",
            tags = {"User Controller"})
    public ResponseEntity<?> deleteUserByEmail(@RequestParam String email) {
        log.info("Trying to delete user with email: {}", email);
        return this.userService.deleteUserByEmail(email);
    }

    @GetMapping("/get/name")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Find a specific user by name",
            description = "Delete a specific user by their first name and last name",
            tags = {"User Controller"})
    public ResponseEntity<?> getUserByName(@RequestParam("first") String firstName,
                                           @RequestParam("last") String lastName){
        log.info("Trying to fetch user {} {}", firstName, lastName);
        ResponseEntity<?> response = this.userService.getUsersByName(firstName, lastName);
        log.info("{}", response);
        return response;
    }

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

    @GetMapping("/get/advanced")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Find a specific user by their age and last name",
            description = "Find a specific user by their age and the first digit on their last name",
            tags = {"User Controller"})
    public ResponseEntity<?> getUserByAgeAndLastName(@RequestParam Integer lower,
                                                @RequestParam Integer upper,
                                                @RequestParam String startingChar) {
        log.info("Trying to fetch users by advanced querying: Lower bound: {}, Upper bound: {}, " +
                "Starting char at last name: {}", lower, upper, startingChar);
        ResponseEntity<?> response = this.userService.getUserByAgeAndName(lower, upper, startingChar);
        log.info("{}", response);
        return response;
    }

    @GetMapping("/getUser/{id}/info")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get only social information about a specific user",
            description = "Get a userDTO that contains only some information about a user we want to present",
            tags = {"User Controller"})
    public ResponseEntity<?> getUserInfo(@PathVariable Long id) {
        log.info("Trying to get user info by ID: " + id);
        ResponseEntity<?> response = this.userService.getUserDtoInfo(id);
        log.info("{}", response);
        return response;
    }

    @GetMapping("/getAllUsers/info")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all the social information about the users",
            description = "Get all userDTO-s that contain only some information we to present about the users",
            tags = {"User Controller"})
    public ResponseEntity<?> getAllUsersInfo() {
        log.info("Trying to get all users info");
        ResponseEntity<?> response = this.userService.getAllUsersDtoInfo();
        log.info("{}", response);
        return response;
    }
}