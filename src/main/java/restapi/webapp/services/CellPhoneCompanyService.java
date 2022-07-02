package restapi.webapp.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import restapi.webapp.entities.CellPhoneCompanyEntity;
import restapi.webapp.factories.CellPhoneCompanyAssembler;
import restapi.webapp.repos.CellPhoneCompanyRepo;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
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
    private final HashMap<String, Function<String, CellPhoneCompanyEntity>> methodsByParamsMap;

    @Autowired
    public CellPhoneCompanyService(CellPhoneCompanyRepo cellPhoneCompanyRepo, CellPhoneCompanyAssembler cellPhoneCompanyAssembler) {
        this.cellPhoneCompanyRepo = cellPhoneCompanyRepo;
        this.cellPhoneCompanyAssembler = cellPhoneCompanyAssembler;

        this.methodsByParamsMap = new HashMap<>();
        this.methodsByParamsMap.put("name", cellPhoneCompanyRepo::getCellPhoneCompanyByCompanyName);
        this.methodsByParamsMap.put("id", id -> cellPhoneCompanyRepo.getCellPhoneCompanyByCellPhoneCompanyId(Long.valueOf(id)));
    }

    /**
     * A method that fetches all the cell phone companies that the DB contains, if any.
     * @return ResponseEntity of returned cell phone companies.
     */
    public ResponseEntity<?> getAllCompanies(){
        CollectionModel<EntityModel<CellPhoneCompanyEntity>> companies = cellPhoneCompanyAssembler.toCollectionModel(cellPhoneCompanyRepo.findAll());
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
        EntityModel<CellPhoneCompanyEntity> companyEntityModel = cellPhoneCompanyAssembler.toModel(companyEntity);
        return ResponseEntity.of(Optional.of(companyEntityModel));
    }

    /**
     * A method that deletes a cell phone company from the DB according to inputted company name, if exists.
     * @param companyName Company name of wanted cell phone company to be deleted
     * @return ResponseEntity of corresponding message.
     */
    public ResponseEntity<?> deleteCompanyByName(@NonNull String companyName){
        cellPhoneCompanyRepo.deleteCellPhoneCompanyByCompanyName(companyName);
        return ResponseEntity.ok("Cell Phone company " + companyName + " has been deleted.");
    }

    /**
     * A method that deletes a cell phone company from the DB according to inputted ID, if exists.
     * @param id ID of wanted cell phone company to be deleted.
     * @return ResponseEntity of corresponding message.
     */
    public ResponseEntity<?> deleteCompanyById(@NonNull Long id){
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
        CollectionModel<EntityModel<CellPhoneCompanyEntity>> companies = cellPhoneCompanyAssembler.toCollectionModel(cellPhoneCompanyRepo.getCellPhoneCompaniesByUserId(id));
        return ResponseEntity.of(Optional.of(companies));
    }
}
