package restapi.webapp.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

import static java.net.HttpURLConnection.*;

@Component
@RestController
@RequestMapping("/api")
//@Validated
@ApiResponses(value = {
        @ApiResponse(code = HTTP_UNAUTHORIZED, message = "You are not authorized"),
        @ApiResponse(code = HTTP_FORBIDDEN, message = "You don't have permission to access this resource"),
        @ApiResponse(code = HTTP_BAD_REQUEST, message = "Server can't process the request"),
        @ApiResponse(code = HTTP_INTERNAL_ERROR, message = "Server error occurred")
})
@Slf4j
public class ApiController {

    private final ApiService apiService;

    @Autowired
    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/getRandom")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get random user",
            notes = "Get random user from external API")
    public ResponseEntity<?> getRandomUser() {
        log.info("Trying to get random user");
        CompletableFuture<UserEntity> response = this.apiService.getUserByType("random");
        response.join();
        try {
            return ResponseEntity.of(Optional.of(response.get()));
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/getMale")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get male user",
            notes = "Get male user from external API")
    public ResponseEntity<?> getMaleUser() {
        log.info("Trying to get male user");
        CompletableFuture<UserEntity> response = this.apiService.getUserByType("male");
        response.join();
        try {
            return ResponseEntity.of(Optional.of(response.get()));
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/getFemale")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get female user",
            notes = "Get female user from external API")
    public ResponseEntity<?> getFemaleUser() {
        log.info("Trying to get female user");
        CompletableFuture<UserEntity> response = this.apiService.getUserByType("female");
        response.join();
        try {
            return ResponseEntity.of(Optional.of(response.get()));
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

   /* @PostMapping("/saveBySeed/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get and save user by specific seed",
            code = 200,
            notes = "Get female user from external API") // change notes
    public ResponseEntity<?> saveBySeed(@PathVariable String id) {
        log.info("Trying to save user by seed: {}", id);
        //ResponseEntity<?> response = this.apiService.saveBySeed(String id);
        return null;
    }*/

}