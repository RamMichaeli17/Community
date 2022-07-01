package restapi.webapp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import restapi.webapp.entities.UserEntity;
import restapi.webapp.services.ApiService;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * A class that represents the controller of the API, containing various endpoints for getting users and saving them
 */
@Component
@RestController
@RequestMapping("/api")
@Slf4j
@Tag(name = "API Controller", description = "The controller of the API to fetch users from")
public class ApiController {
    private final ApiService apiService;

    @Autowired
    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    /**
     * A method that fetches a random user from external API by requested type (random/male/female)
     * @param gender Requested gender of user to fetch
     * @return ResponseEntity of the returned user
     */
    @GetMapping("/get/{gender}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Fetch a random user by requested type",
            description = "Fetch a random user from external API by specified type (random/male/female)",
            tags = {"API Controller"})
    public ResponseEntity<?> getUserByGender(@PathVariable String gender) {
        log.info("Trying to get {} user", gender);
        CompletableFuture<UserEntity> response = this.apiService.getUserByType(gender);

        // Get the result of CompletableFuture
        response.join();
        log.info("{}", response);
        try {
            return ResponseEntity.of(Optional.of(response.get()));
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * A method that fetches a random user by requested type from external API, and saves it to the DB.
     * @param gender Requested gender of user to fetch
     * @return ResponseEntity of the saved user
     */
    @PostMapping("/save/{gender}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Save a Random user by requested type",
            description = "Save a random user from external API by specified type (random/male/female)",
            responses = {@ApiResponse(responseCode = "201", description = "User created")},
            tags = {"API Controller"})
    public ResponseEntity<?> saveUserByGender(@PathVariable String gender) {
        log.info("Trying to save user by type: {}", gender);
        CompletableFuture<UserEntity> response = this.apiService.getUserByType(gender);

        // Get the result of CompletableFuture
        response.join();
        log.info("{}", response);
        try {
            return this.apiService.saveUser(response.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

}