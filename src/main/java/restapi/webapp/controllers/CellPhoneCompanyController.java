package restapi.webapp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import restapi.webapp.entities.CellPhoneCompany;
import restapi.webapp.services.CellPhoneService;

@RestController
@RequestMapping("/company")
@Slf4j
@Tag(name = "Cell Phone Company Controller", description = "The controller of CellPhone Company entity")
public class CellPhoneCompanyController {
    private final CellPhoneService cellPhoneService;

    @Autowired
    public CellPhoneCompanyController(CellPhoneService cellPhoneService) {
        this.cellPhoneService = cellPhoneService;
    }

    @GetMapping("/getAllCompanies")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Find all companies",
            description = "Find all of Cell Phone companies and their details",
            tags = {"Cell Phone Company Controller"})
    public ResponseEntity<?> getAllCompanies() {
        log.info("Trying to fetch all cell phone companies");
        ResponseEntity<?> response = this.cellPhoneService.getAllCompanies();
        log.info("{}", response);
        return response;
    }

    @GetMapping("/get/{param}/{value}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Find a Cell Phone company by Path Variable params",
            description = "Find company details by param and value",
            tags = {"Cell Phone Company Controller"})
    public ResponseEntity<?> getCompanyWithPathVar(@PathVariable String param, @PathVariable String value) {
        log.info("Trying to get company by param \"{}\" and value \"{}\"", param, value);
        return this.cellPhoneService.getCompanyBySpecificParameter(param, value);
    }

    @Transactional
    @DeleteMapping("/deleteByName")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete a company by its name",
            description = "Delete a specific company by their company name",
            tags = {"Cell Phone Company Controller"})
    public ResponseEntity<?> deleteCompanyByName(@RequestParam String name) {
        log.info("Trying to delete cell phone company with name: {}", name);
        return this.cellPhoneService.deleteCompany(name);
    }

    @Transactional
    @DeleteMapping("/deleteById")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete a company by its ID",
            description = "Delete a specific company by their company ID",
            tags = {"Cell Phone Company Controller"})
    public ResponseEntity<?> deleteCompanyById(@RequestParam String id) {
        log.info("Trying to delete cell phone company with ID: {}", id);
        return this.cellPhoneService.deleteCompany(id);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update a cell phone company",
            description = "Update a specific cell phone company",
            tags = {"Cell Phone Company Controller"})
    public ResponseEntity<?> updateUser(@RequestBody CellPhoneCompany company) {
        log.info("Trying to update company by specific parameters");
        log.info("{}", company);
        return this.cellPhoneService.updateCompany(company);
    }

    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a cell phone company",
            description = "Create a new company by specific parameters",
            responses = {@ApiResponse(responseCode = "201", description = "Cell Phone Company created")},
            tags = {"Cell Phone Company Controller"})
    public ResponseEntity<?> createCompany(@RequestBody CellPhoneCompany company) {
        log.info("Trying to create a new company by specific parameters:");
        log.info("{}", company);
        return this.cellPhoneService.createCompany(company);
    }
}
