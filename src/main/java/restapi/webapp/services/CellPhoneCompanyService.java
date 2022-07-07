package restapi.webapp.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import restapi.webapp.dtos.CellPhoneCompanyDTO;
import restapi.webapp.dtos.UserDTO;
import restapi.webapp.entities.CellPhoneCompanyEntity;
import restapi.webapp.entities.UserEntity;
import restapi.webapp.exceptions.*;
import restapi.webapp.factories.CellPhoneCompanyAssembler;
import restapi.webapp.factories.CellPhoneCompanyDTOAssembler;
import restapi.webapp.repos.CellPhoneCompanyRepo;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A class that operates as the service of a cell phone company entity, containing the business logic of
 * the operations that are given to the cell phone company.
 */
@Service
@Slf4j
public class CellPhoneCompanyService {
    private final CellPhoneCompanyRepo cellPhoneCompanyRepo;
    private final CellPhoneCompanyAssembler cellPhoneCompanyAssembler;
    private final CellPhoneCompanyDTOAssembler cellPhoneCompanyDTOAssembler;
    private final HashMap<String, Function<String, CellPhoneCompanyEntity>> methodsByParamsMap;

    @Autowired
    public CellPhoneCompanyService(CellPhoneCompanyRepo cellPhoneCompanyRepo, CellPhoneCompanyAssembler cellPhoneCompanyAssembler, CellPhoneCompanyDTOAssembler cellPhoneCompanyDTOAssembler) {
        this.cellPhoneCompanyRepo = cellPhoneCompanyRepo;
        this.cellPhoneCompanyAssembler = cellPhoneCompanyAssembler;
        this.cellPhoneCompanyDTOAssembler = cellPhoneCompanyDTOAssembler;

        this.methodsByParamsMap = new HashMap<>();
        this.methodsByParamsMap.put("name", cellPhoneCompanyRepo::getCellPhoneCompanyByCompanyName);
        this.methodsByParamsMap.put("id", id -> cellPhoneCompanyRepo.getCellPhoneCompanyByCellPhoneCompanyId(Long.valueOf(id)));
    }

    /**
     * A method that fetches all the cell phone companies that the DB contains, if any.
     * @return ResponseEntity of returned cell phone companies.
     */
    public ResponseEntity<?> getAllCompanies(){
        List<CellPhoneCompanyEntity> companyEntities = cellPhoneCompanyRepo.findAll();
        companyEntities.stream().findAny().orElseThrow(CompaniesNotFoundException::new);
        CollectionModel<EntityModel<CellPhoneCompanyEntity>> companies =
                cellPhoneCompanyAssembler.toCollectionModel(companyEntities);
        return ResponseEntity.of(Optional.of(companies));
    }

    /**
     * A method that fetches a cell phone company from the DB according to the requested parameter
     * and value that the user inputs, if exists.
     * @param param The requested parameter that the search is based on
     * @param value The requested value of the inputted parameter
     * @return ResponseEntity of the cell phone company, if exists.
     */
    public ResponseEntity<?> getCompanyBySpecificParameter(@NonNull String param, @NonNull String value) {
        CellPhoneCompanyEntity companyEntity = this.methodsByParamsMap.get(param).apply(value);

        boolean isValueDigitsOnly = value.matches("\\d+");

        if (Objects.isNull(companyEntity) && isValueDigitsOnly){
            throw new CompanyNotFoundException(Long.valueOf(value));
        }
        else if(Objects.isNull(companyEntity)){
            throw new CompanyNotFoundException(value);
        }

        EntityModel<CellPhoneCompanyEntity> companyEntityModel = cellPhoneCompanyAssembler.toModel(companyEntity);
        return ResponseEntity.of(Optional.of(companyEntityModel));
    }

    /**
     * A method that deletes a cell phone company from the DB according to inputted company name, if exists.
     * @param companyName Company name of wanted cell phone company to be deleted
     * @return ResponseEntity of corresponding message.
     */
    public ResponseEntity<?> deleteCompanyByName(@NonNull String companyName){
        CellPhoneCompanyEntity companyEntity = cellPhoneCompanyRepo.getCellPhoneCompanyByCompanyName(companyName);
        if(Objects.isNull(companyEntity)){
            throw new CompanyNotFoundException(companyName);
        }
        cellPhoneCompanyRepo.deleteCellPhoneCompanyFromUserCompaniesTableByCompanyName(companyName);
        cellPhoneCompanyRepo.deleteCellPhoneCompanyByCompanyName(companyName);
        return ResponseEntity.ok("Cell Phone company " + companyName + " has been deleted.");
    }

    /**
     * A method that deletes a cell phone company from the DB according to inputted ID, if exists.
     * @param id ID of wanted cell phone company to be deleted.
     * @return ResponseEntity of corresponding message.
     */
    public ResponseEntity<?> deleteCompanyById(@NonNull Long id){
        cellPhoneCompanyRepo.findById(id).orElseThrow(() -> new CompanyNotFoundException(id));
        cellPhoneCompanyRepo.deleteCellPhoneCompanyFromUserCompaniesTableById(id);
        cellPhoneCompanyRepo.deleteCellPhoneCompanyByCellPhoneCompanyId(id);
        return ResponseEntity.ok("Cell Phone company with ID " + id + " has been deleted.");
    }

    /**
     * A method that gets a cell phone company entity as a parameter,
     * updates the corresponding fields and saves into the DB.
     * @param company Cell phone company entity to be updated.
     * @return ResponseEntity of the updated cell phone company.
     */
    public ResponseEntity<?> updateCompany(@NonNull CellPhoneCompanyEntity company){
        cellPhoneCompanyRepo.findById(company.getCellPhoneCompanyId()).orElseThrow(() ->
                new CompanyNotFoundException(company.getCellPhoneCompanyId()));
        //TODO: should throw exception: this name is not available instead of "500 error"
//        CellPhoneCompanyEntity cellPhoneCompanyEntity = cellPhoneCompanyRepo.getCellPhoneCompanyByCellPhoneCompanyId(company.getCellPhoneCompanyId());
//        if (Objects.isNull(cellPhoneCompanyEntity)) {
//            throw new CompanyExistsException(company.getCompanyName());
//        }
        cellPhoneCompanyRepo.save(company);
        log.info("Company {} has been updated", company.getCompanyName());
        return ResponseEntity.of(Optional.of(cellPhoneCompanyAssembler.toModel(company)));
    }

    /**
     * A method that gets a cell phone company entity as a parameter, saves it into the DB,
     * and returns the created cell phone company.
     * @param company Cell phone company entity to be inserted into the DB.
     * @return ResponseEntity of the created cell phone company.
     */
    public ResponseEntity<?> createCompany(@NonNull CellPhoneCompanyEntity company){
        CellPhoneCompanyEntity companyEntity = cellPhoneCompanyRepo.getCellPhoneCompanyByCompanyName
                (company.getCompanyName());
        if(!Objects.isNull(companyEntity)){ // If there was already a company with the same name
            throw new CompanyExistsException(company.getCompanyName());
        }
        cellPhoneCompanyRepo.save(company);
        log.info("company {} has been created", company.getCompanyName());
        return ResponseEntity.of(Optional.of(cellPhoneCompanyAssembler.toModel(company)));
    }

    /**
     * A method that returns all the cell phone companies that belong to a user, if there are any.
     * @param id The ID of the user that the search is based on.
     * @return ResponseEntity of the returned cell phone companies that exist.
     */
    public ResponseEntity<?> getCellPhoneCompaniesByUserId(@NonNull Long id) {
        CollectionModel<EntityModel<CellPhoneCompanyEntity>> companies =
                cellPhoneCompanyAssembler.toCollectionModel(cellPhoneCompanyRepo.getCellPhoneCompaniesByUserId(id));
        if(companies.getContent().isEmpty()){
            throw new CompaniesNotFoundException();
        }
        return ResponseEntity.of(Optional.of(companies));
    }

    /**
     * A method that fetches a company from the DB by its company ID if exists, then convert it
     * into its DTO representation.
     * @param id ID of company to be fetched
     * @return ResponseEntity of the requested company, if exists.
     */
    public ResponseEntity<?> getCellPhoneCompanyDtoInfo(@NonNull Long id) {
        /*
        This method uses the pre-defined repo method findById because it returns an Optional<T>,
        which allows us to map the returned value into a CellPhoneCompanyDTO and return an exception easily,
        in case of an error.
         */

        return cellPhoneCompanyRepo.findById(id)
                .map(CellPhoneCompanyDTO::new)
                .map(cellPhoneCompanyDTOAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new CompanyNotFoundException(id));
    }

    /**
     * A method that fetches all the companies on DB if they exist,
     * then convert them into their DTO representation.
     * @return ResponseEntity of all the companies on DB, if they exist.
     */
    public ResponseEntity<?> getAllCellPhoneCompaniesDtoInfo() {
        List<CellPhoneCompanyEntity> companyEntities = cellPhoneCompanyRepo.findAll();
        // Check if there are any users that exist in DB
        companyEntities.stream().findAny().orElseThrow(CompaniesNotFoundException::new);
        return ResponseEntity.ok(cellPhoneCompanyDTOAssembler.toCollectionModel(companyEntities
                .stream()
                .map(CellPhoneCompanyDTO::new)
                .collect(Collectors.toList())));
    }

}
