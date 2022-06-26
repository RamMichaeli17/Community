package restapi.webapp.controllers;

/*import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;*/
import io.swagger.v3.oas.annotations.Operation;
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
import static java.net.HttpURLConnection.*;

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

    @GetMapping("/get/{type}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Fetch a random user by requested type",
            description = "Fetch a random user from external API by specified type (male/female)",
            tags = {"API Controller"})
    public ResponseEntity<?> getUserByType(@PathVariable String type) {
        log.info("Trying to get {} user", type);
        CompletableFuture<UserEntity> response = this.apiService.getUserByType(type);
        response.join();
        log.info("{}", response);
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