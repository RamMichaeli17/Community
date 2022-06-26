package restapi.webapp.controllers;

/*import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;*/
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
/*@ApiResponses(value = {
        @ApiResponse(code = HTTP_UNAUTHORIZED, message = "You are not authorized"),
        @ApiResponse(code = HTTP_FORBIDDEN, message = "You don't have permission to access this resource"),
        @ApiResponse(code = HTTP_BAD_REQUEST, message = "Server can't process the request"),
        @ApiResponse(code = HTTP_INTERNAL_ERROR, message = "Server error occurred")
})*/
public class ApiController {
    private final ApiService apiService;

    @Autowired
    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/get/{type}")
    @ResponseStatus(HttpStatus.OK)
    /*@ApiOperation(value = "Get a user by requested type")
            //notes = "Get a requested type of user from external API")*/
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