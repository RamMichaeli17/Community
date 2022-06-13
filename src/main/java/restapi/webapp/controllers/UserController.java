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

import java.util.HashMap;
import java.util.concurrent.Callable;

import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;

@RestController
@RequestMapping("/control")
//@Validated
@ApiResponses(value = {
        @ApiResponse(code = HTTP_UNAUTHORIZED, message = "You are not authorized"),
        @ApiResponse(code = HTTP_FORBIDDEN, message = "You don't have permission to access this resource"),
        @ApiResponse(code = HTTP_BAD_REQUEST, message = "Server can't process the request"),
        @ApiResponse(code = HTTP_INTERNAL_ERROR, message = "Server error occurred")
})
@Slf4j
public class UserController {

    private final UserService userService;
    private HashMap<String, Callable> callMethodsByParam;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;

        callMethodsByParam = new HashMap<>();
    }

    @GetMapping("/users/search/{email}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getUserByEmail(@PathVariable String email){
        ResponseEntity<?> response = userService.getUserByEmail(email);
        return response;
    }

    @Transactional
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Find all users",
            notes = "Find user details by name, location age and more")
    public ResponseEntity<?> getAllUsers() {
        log.info("Trying to fetch all users");
        ResponseEntity<?> response = this.userService.getAllUsers();
        return response;
    }

    /*@GetMapping("/find/{param}/{value}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Find a user by specific parameters",
            notes = "Find user details by name, location age and more")
    public ResponseEntity<?> getUserWithPathVar(@PathVariable String param, @PathVariable String value) {
        log.info("Trying to fetch users by parameter `{}` with value `{}`", param, value);
        ResponseEntity<?> response = this.userService.getUserBySpecificParameter(param, value);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Find a user by specific parameters",
            code = 200,
            notes = "Find user details by name, location age and more")
    public ResponseEntity<?> getUserWithRequestParam(@RequestParam String param, @RequestParam String value) {
        log.info("Trying to fetch users by parameter `{}` with value `{}`", param, value);
        //ResponseEntity<?> response = this.userService.getUserBySpecificParameter(param, value);
        return null;
    }

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

    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a user",
            code = 201,
            notes = "Create new user by specific parameters")
    public ResponseEntity<?> createUser(@RequestBody UserEntity user) {
        log.info("Trying to create new user by specific parameters:");
        log.info("{}", user);
        ResponseEntity<?> response = this.userService.createUser(user);
        return response;
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update a user",
            notes = "Update a user by specific parameters")
    public ResponseEntity<?> updateUser(@RequestBody UserEntity user) {
        log.info("Trying to update user by specific parameters");
        log.info("{}", user);
        ResponseEntity<?> response = this.userService.updateUser(user);
        return response;
    }

    @Transactional
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Delete a user",
            notes = "Delete a user by id")
    public ResponseEntity<?> deleteUser(@RequestParam String id) {
        log.info("Trying to delete user with id: {}", id);
        ResponseEntity<?> response = this.userService.deleteUser(id);
        log.info("{}", response);
        return response;
    }

    @GetMapping("/getbyname")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getUserByName(@RequestParam("firstName") String firstName,
                                           @RequestParam("lastName") String lastName){
        log.info("Trying to fetch user {} {}", firstName, lastName);
        ResponseEntity<?> response = this.userService.getUserByName(firstName, lastName);
        log.info("{}", response);
        return response;
    }

    @GetMapping("/getbygender")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getListOfUsersByGender(@RequestParam("gender") String gender){
        log.info("Trying to fetch users by gender: {}", gender);
        ResponseEntity<?> response = this.userService.getUsersByGender(gender);
        log.info("{}", response);
        return response;
    }

    @GetMapping("/getbyage")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getListOfUsersByAge(@RequestParam("age") Integer age){
        log.info("Trying to fetch users by age: {}", age);
        ResponseEntity<?> response = this.userService.getUsersByAge(age);
        log.info("{}", response);
        return response;
    }

    @GetMapping("/getbyphone")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getUserByPhone(@RequestParam("phone") String phone){
        log.info("Trying to fetch user by phone: {}", phone);
        ResponseEntity<?> response = this.userService.getUserByPhone(phone);
        log.info("{}", response);
        return response;
    }

    @GetMapping("/getbylocation")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getUsersByLocation(@RequestParam("street") String street,
                                                @RequestParam("city") String city,
                                                @RequestParam("state") String state){
        log.info("Trying to fetch users by location: {}, {}, {}", street, city, state);
        ResponseEntity<?> response = this.userService.getUserByLocation(street, city, state);
        log.info("{}", response);
        return response;
    }
}