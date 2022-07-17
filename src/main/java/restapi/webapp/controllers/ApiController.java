package restapi.webapp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import restapi.webapp.entities.CellPhoneCompanyEntity;
import restapi.webapp.entities.UserEntity;
import restapi.webapp.exceptions.APIException;
import restapi.webapp.services.ApiService;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * A class that represents the controller of the API,
 * containing various endpoints for getting users and saving them.
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
     * A method that fetches a random user from external API by requested type (random/male/female).
     * @param gender Requested gender of user to be fetched.
     * @return ResponseEntity of the returned user.
     */
    @GetMapping("/get/user/{gender}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Fetch a random user by requested type",
            description = "Fetch a random user from external API by specified type (random/male/female)",
            tags = {"API Controller"})
    public ResponseEntity<UserEntity> getUserByGender(@PathVariable String gender) {
        log.info("Trying to get {} user", gender);
        CompletableFuture<UserEntity> response = this.apiService.getUserByGender(gender);

        // Get the result of CompletableFuture
        response.join();

        log.info("{}", response);
        try {
            return ResponseEntity.of(Optional.of(response.get()));
        } catch (InterruptedException | ExecutionException e) {
            throw new APIException();
        }
    }

    /**
     * A method that fetches a random user by requested type from external API, and saves it to the DB.
     * @param gender Requested gender of user to fetch.
     * @return ResponseEntity of the saved user.
     */
    @PostMapping("/save/user/{gender}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Save a Random user by requested type",
            description = "Save a random user from external API by specified type (random/male/female)",
            responses = {@ApiResponse(responseCode = "201", description = "User created")},
            tags = {"API Controller"})
    public ResponseEntity<EntityModel<UserEntity>> saveUserByGender(@PathVariable String gender) {
        log.info("Trying to save user by gender: {}", gender);
        return this.apiService.saveUser(this.apiService.getUserByGender(gender).join());
    }

    /**
     * A method that fetches a random company from external API.
     * @return ResponseEntity of the returned company.
     */
    @GetMapping("/get/company")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Fetch a random company",
            description = "Fetch a random company from external API",
            tags = {"API Controller"})
    public ResponseEntity<CellPhoneCompanyEntity> getCompany() {
        log.info("Trying to get company");
        CompletableFuture<CellPhoneCompanyEntity> response = this.apiService.getRandomCompany();

        // Get the result of CompletableFuture
        response.join();
        log.info("{}", response);
        try {
            return ResponseEntity.of(Optional.of(response.get()));
        } catch (InterruptedException | ExecutionException e) {
            throw new APIException();
        }
    }

    /**
     * A method that creates a radom cell phone company based on random name and countries.
     * @return ResponseEntity of the created cell phone company.
     */
    @PostMapping(value = "save/company")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a radnom cell phone company",
            description = "Create a new company by random values",
            responses = {@ApiResponse(responseCode = "201", description = "Random Cell Phone Company created")},
            tags = {"Cell Phone Company Controller"})
    public ResponseEntity<EntityModel<CellPhoneCompanyEntity>> saveRandomCompany() {
        log.info("Trying to save a random company:");
        return this.apiService.saveCompany(this.apiService.getRandomCompany().join());
    }
}