package restapi.webapp.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import restapi.webapp.entities.CellPhoneCompany;
import restapi.webapp.factories.CellPhoneCompanyAssembler;
import restapi.webapp.repos.CellPhoneCompanyRepo;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@Slf4j
public class CellPhoneService {
    private final CellPhoneCompanyRepo cellPhoneCompanyRepo;
    private final CellPhoneCompanyAssembler cellPhoneCompanyAssembler;
    private final HashMap<String, Function<String, List<CellPhoneCompany>>> methodsByParamsMap;

    @Autowired
    public CellPhoneService(CellPhoneCompanyRepo cellPhoneCompanyRepo, CellPhoneCompanyAssembler cellPhoneCompanyAssembler) {
        this.cellPhoneCompanyRepo = cellPhoneCompanyRepo;
        this.cellPhoneCompanyAssembler = cellPhoneCompanyAssembler;

        this.methodsByParamsMap = new HashMap<>();
        this.methodsByParamsMap.put("name", cellPhoneCompanyRepo::getCellPhoneCompanyByCompanyName);
        this.methodsByParamsMap.put("id", id -> cellPhoneCompanyRepo.getCellPhoneCompanyByCellPhoneCompanyId(Long.valueOf(id)));
    }

    //todo: extract to public class utilities
    private ResponseEntity<? extends RepresentationModel<? extends RepresentationModel<?>>> getCorrespondingEntityType
            (List<CellPhoneCompany> companyEntities) {
        if (companyEntities.size() == 1) {
            CellPhoneCompany companyEntity = companyEntities.get(0);
            EntityModel<CellPhoneCompany> companyEntityModel = cellPhoneCompanyAssembler.toModel(companyEntity);
            return ResponseEntity.of(Optional.of(companyEntityModel));
        }
        CollectionModel<EntityModel<CellPhoneCompany>> companyEntitiesModel = cellPhoneCompanyAssembler.toCollectionModel(companyEntities);
        return ResponseEntity.of(Optional.of(companyEntitiesModel));
    }

    public ResponseEntity<?> getAllCompanies(){
        CollectionModel<EntityModel<CellPhoneCompany>> companies = cellPhoneCompanyAssembler.toCollectionModel(cellPhoneCompanyRepo.findAll());
        return ResponseEntity.of(Optional.of(companies));
    }

    public ResponseEntity<?> getCompanyBySpecificParameter(@NonNull String param, @NonNull String value) {
        List<CellPhoneCompany> companyEntities = this.methodsByParamsMap.get(param).apply(value);
        return getCorrespondingEntityType(companyEntities);
    }

    public ResponseEntity<?> deleteCompanyByName(@NonNull String companyName){
        cellPhoneCompanyRepo.deleteCellPhoneCompanyByCompanyName(companyName);
        return ResponseEntity.ok("Cell Phone company " + companyName + " has been deleted.");
    }

    public ResponseEntity<?> deleteCompanyById(@NonNull Long id){
        cellPhoneCompanyRepo.deleteCellPhoneCompanyByCellPhoneCompanyId(id);
        return ResponseEntity.ok("Cell Phone company with ID " + id + " has been deleted.");
    }

    public ResponseEntity<?> updateCompany(@NonNull CellPhoneCompany company){
        cellPhoneCompanyRepo.save(company);
        log.info("Company {} has been updated", company.getCompanyName());
        return ResponseEntity.of(Optional.of(cellPhoneCompanyAssembler.toModel(company)));
    }

    public ResponseEntity<?> createCompany(@NonNull CellPhoneCompany company){
        cellPhoneCompanyRepo.save(company);
        log.info("company {} has been created", company.getCompanyName());
        return ResponseEntity.of(Optional.of(cellPhoneCompanyAssembler.toModel(company)));
    }



}
