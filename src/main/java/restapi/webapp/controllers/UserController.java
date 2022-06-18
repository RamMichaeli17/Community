package restapi.webapp.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import restapi.webapp.entities.UserEntity;
import restapi.webapp.services.UserService;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;

@RestController
@RequestMapping("/control")
@ApiResponses(value = {
        @ApiResponse(code = HTTP_UNAUTHORIZED, message = "You are not authorized"),
        @ApiResponse(code = HTTP_FORBIDDEN, message = "You don't have permission to access this resource"),
        @ApiResponse(code = HTTP_BAD_REQUEST, message = "Server can't process the request"),
        @ApiResponse(code = HTTP_INTERNAL_ERROR, message = "Server error occurred")
})
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getAllUsers")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Find all users",
            notes = "Find user details by name, location age and more")
    public ResponseEntity<?> getAllUsers() {
        log.info("Trying to fetch all users");
        return this.userService.getAllUsers();
    }

    @GetMapping("/find/{param}/{value}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Find a user by specific parameters",
            notes = "Find user details by name, location, age and more")
    public ResponseEntity<?> getUserWithPathVar(@PathVariable String param, @PathVariable String value) {
        log.info("Trying to get user by param \"{}\" and value \"{}\"", param, value);
        return this.userService.getUserBySpecificParameter(param, value);
    }

    @GetMapping("/find")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Find a user by specific parameters",
            notes = "Find user details by name, location, age and more")
    public ResponseEntity<?> getUserWithRequestParam(@RequestParam String param, @RequestParam String value) {
        log.info("Trying to get user by param \"{}\" and value \"{}\"", param, value);
        return this.userService.getUserBySpecificParameter(param, value);
    }

    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a user",
            code = 201,
            notes = "Create new user by specific parameters")
    public ResponseEntity<?> createUser(@RequestBody UserEntity user) {
        log.info("Trying to create new user by specific parameters:");
        log.info("{}", user);
        return this.userService.createUser(user);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update a user",
            notes = "Update a user by specific parameters")
    public ResponseEntity<?> updateUser(@RequestBody UserEntity user) {
        log.info("Trying to update user by specific parameters");
        log.info("{}", user);
        return this.userService.updateUser(user);
    }

    /*
    In the declarative approach, we annotate the methods with the @Transactional annotation.
    The @Transactional annotation makes use of the attributes rollbackFor or rollbackForClassName to rollback the transactions,
    and the attributes noRollbackFor or noRollbackForClassName to avoid rollback on listed exceptions.
     */
    @Transactional
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Delete a user",
            notes = "Delete a user by id")
    public ResponseEntity<?> deleteUser(@RequestParam Long id) {
        log.info("Trying to delete user with id: {}", id);
        return this.userService.deleteUser(id);
    }

//    @GetMapping("/name")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<?> getUserByName(@RequestParam("firstName") String firstName,
//                                           @RequestParam("lastName") String lastName){
//        log.info("Trying to fetch user {} {}", firstName, lastName);
//        ResponseEntity<?> response = this.userService.getUserByName(firstName, lastName);
//        log.info("{}", response);
//        return response;
//    }
//
//    @GetMapping("/location")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<?> getUsersByLocation(String city, String street, String country){
//        log.info("Trying to fetch users by location: {}, {}, {}", city, street, country);
//        ResponseEntity<?> response = this.userService.getUsersByLocation(city, street, country);
//        log.info("{}", response);
//        return response;
//    }

    /*
    @GetMapping("/find/comp")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Find a user by advanced calculations",
            code = 200,
            notes = "Find user details by advanced calculations")
    public ResponseEntity<?> getUserByAdvCalc() {
        log.info("Trying to fetch users by advanced calculations");
        //ResponseEntity<?> response = this.userService.getUserByAdvCalc();
        return null;
    }*/
}