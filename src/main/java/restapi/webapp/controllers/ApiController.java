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
import restapi.webapp.services.ApiService;

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

    private ApiService apiService;

    @Autowired
    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/getRandom")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get random user",
            code = 200,
            notes = "Get random user from external API")
    public ResponseEntity<?> getRandomUser() {
        log.info("Trying to get random user");
        ResponseEntity<?> response = ResponseEntity.ok(this.apiService.getRandomUser());
        return response;
    }

    @GetMapping("/getMale")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get male user",
            code = 200,
            notes = "Get male user from external API")
    public ResponseEntity getMaleUser() {
        log.info("Trying to get male user");
        ResponseEntity<?> response = ResponseEntity.ok(this.apiService.getMaleUser());
        return response;
    }

    @GetMapping("/getFemale")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get female user",
            code = 200,
            notes = "Get female user from external API")
    public ResponseEntity getFemaleUser() {
        log.info("Trying to get female user");
        ResponseEntity<?> response = ResponseEntity.ok(this.apiService.getFemaleUser());
        return response;
    }

    @PostMapping("/saveBySeed/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get and save user by specific seed",
            code = 200,
            notes = "Get female user from external API")
    public ResponseEntity saveBySeed(@PathVariable String id) {
        log.info("Trying to save user by seed: {}", id);
        //ResponseEntity<?> response = this.apiService.saveBySeed(String id);
        return null;
    }

}