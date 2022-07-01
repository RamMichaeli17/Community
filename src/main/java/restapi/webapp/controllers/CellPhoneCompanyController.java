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
import restapi.webapp.entities.CellPhoneCompanyEntity;
import restapi.webapp.services.CellPhoneCompanyService;

/**
 * A class that represents the controller of a cell phone company,
 * containing various endpoints for getting information about companies,
 * searching companies by specific parameters, editing company's information,
 * as well as creation and deletion of companies
 */
@RestController
@RequestMapping("/companies")
@Slf4j
@Tag(name = "Cell Phone Company Controller", description = "The controller of CellPhone Company entity")
public class CellPhoneCompanyController {
    private final CellPhoneCompanyService cellPhoneCompanyService;

    @Autowired
    public CellPhoneCompanyController(CellPhoneCompanyService cellPhoneCompanyService) {
        this.cellPhoneCompanyService = cellPhoneCompanyService;
    }

    /**
     * A method that returns all the cell phone companies that exist in the DB, if there are any.
     * @return ResponseEntity of the returned cell phone companies that exist.
     */
    @GetMapping("/getAllCompanies")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Find all companies",
            description = "Find all of Cell Phone companies and their details",
            tags = {"Cell Phone Company Controller"})
    public ResponseEntity<?> getAllCompanies() {
        log.info("Trying to fetch all cell phone companies");
        ResponseEntity<?> response = this.cellPhoneCompanyService.getAllCompanies();
        log.info("{}", response);
        return response;
    }

    /**
     * A method that fetches a cell phone company from the DB according to the requested parameter and value that
     * the user inserted, if exists.
     * @param param The requested parameter that the search is based on.
     * @param value The requested value of the inserted parameter.
     * @return ResponseEntity of the cell phone company, if exists.
     */
    @GetMapping("/get/{param}/{value}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Find a Cell Phone company by Path Variable params",
            description = "Find company details by param and value",
            tags = {"Cell Phone Company Controller"})
    public ResponseEntity<?> getCompanyWithPathVar(@PathVariable String param, @PathVariable String value) {
        log.info("Trying to get company by param \"{}\" and value \"{}\"", param, value);
        return this.cellPhoneCompanyService.getCompanyBySpecificParameter(param, value);
    }

    /**
     * A method that deletes a cell phone company from the DB based on the company's name, if exists.
     * @param name Name of the requested cell phone company to be deleted.
     * @return ResponseEntity that contains the corresponding message for the deletion request.
     */
    @Transactional
    @DeleteMapping("/deleteByName")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete a company by its name",
            description = "Delete a specific company by their company name",
            tags = {"Cell Phone Company Controller"})
    public ResponseEntity<?> deleteCompanyByName(@RequestParam String name) {
        log.info("Trying to delete cell phone company with name: {}", name);
        return this.cellPhoneCompanyService.deleteCompanyByName(name);
    }

    /**
     * A method that deletes a cell phone company from the DB based on the company's ID, if exists.
     * @param id ID of the requested company to be deleted.
     * @return ResponseEntity that contains the corresponding message for the deletion request.
     */
    @Transactional
    @DeleteMapping("/deleteById")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete a company by its ID",
            description = "Delete a specific company by their company ID",
            tags = {"Cell Phone Company Controller"})
    public ResponseEntity<?> deleteCompanyById(@RequestParam Long id) {
        log.info("Trying to delete cell phone company with ID: {}", id);
        return this.cellPhoneCompanyService.deleteCompanyById(id);
    }

    /**
     * A method that updates a cell phone company based on the content that is given in the request body, if exists.
     * @param company Cell phone company to be updated.
     * @return ResponseEntity of the updated company, if exists.
     */
    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update a cell phone company",
            description = "Update a specific cell phone company",
            tags = {"Cell Phone Company Controller"})
    public ResponseEntity<?> updateUser(@RequestBody CellPhoneCompanyEntity company) {
        log.info("Trying to update company by specific parameters");
        return this.cellPhoneCompanyService.updateCompany(company);
    }

    /**
     * A method that creates a cell phone company based on the content that is given in the request body.
     * @param company Cell phone company to be created.
     * @return ResponseEntity of the created cell phone company.
     */
    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a cell phone company",
            description = "Create a new company by specific parameters",
            responses = {@ApiResponse(responseCode = "201", description = "Cell Phone Company created")},
            tags = {"Cell Phone Company Controller"})
    public ResponseEntity<?> createCompany(@RequestBody CellPhoneCompanyEntity company) {
        log.info("Trying to create a new company by specific parameters:");
        log.info("{}", company);
        return this.cellPhoneCompanyService.createCompany(company);
    }
}
