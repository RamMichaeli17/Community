package restapi.webapp.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restapi.webapp.entities.UserEntity;
import restapi.webapp.factories.UserEntityAssembler;
import restapi.webapp.services.UserService;

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

    private final UserService service;
    private final UserEntityAssembler assembler;

    @Autowired
    public UserController(UserService service, UserEntityAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @RequestMapping("/test")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create new row in DB",
                code = 201,
                notes = "Create a new row in the DB testing")
    public String test() {
        log.info("Trying to save in DB...");
        return "Test";
    }

    @GetMapping("/users/getallusers")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CollectionModel<EntityModel<UserEntity>>> getAllUsers(){
        return ResponseEntity.ok(assembler.toCollectionModel(service.getAllUsers()));
    }
    @GetMapping("/users/search/{email}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<EntityModel<UserEntity>> getUserByEmail(@PathVariable String email){
        return ResponseEntity.ok(assembler.toModel(service.getUserByEmail(email)));
    }

    /*@GetMapping("users/search/")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<EntityModel<UserEntity>> getUserByName(@RequestParam String firstName,
                                                                 @RequestParam String lastName){
        return ResponseEntity.ok(assembler.toModel(service.getUserByName(firstName, lastName)));
    }*/

    @GetMapping("/users/search/{age}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CollectionModel<EntityModel<UserEntity>>> getUsersByAge(@PathVariable Integer age){
        return ResponseEntity.ok(assembler.toCollectionModel(service.getUsersByAge(age)));
    }

}