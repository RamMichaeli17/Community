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
import restapi.webapp.entities.UserEntity;
import restapi.webapp.factories.CellPhoneCompanyAssembler;
import restapi.webapp.repos.CellPhoneCompanyRepo;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@Slf4j

public class CellPhoneService {
    private final CellPhoneCompanyRepo repo;
    private final CellPhoneCompanyAssembler assembler;
    private final HashMap<String, Function<String, List<CellPhoneCompany>>> methodsByParamsMap;

    @Autowired
    public CellPhoneService(CellPhoneCompanyRepo repo, CellPhoneCompanyAssembler assembler) {
        this.repo = repo;
        this.assembler = assembler;

        this.methodsByParamsMap = new HashMap<>();
        this.methodsByParamsMap.put("name", repo::findCellPhoneCompanyByCompanyName);
        this.methodsByParamsMap.put("id", repo::findCellPhoneCompanyByCellPhoneCompanyId);
    }

    private ResponseEntity<? extends RepresentationModel<? extends RepresentationModel<?>>> returnEntityList
            (List<CellPhoneCompany> companyEntities) {
        if (companyEntities.size() == 1) {
            CellPhoneCompany companyEntity = companyEntities.get(0);
            EntityModel<CellPhoneCompany> companyEntityModel = assembler.toModel(companyEntity);
            return ResponseEntity.of(Optional.of(companyEntityModel));
        }
        CollectionModel<EntityModel<CellPhoneCompany>> companyEntitiesModel = assembler.toCollectionModel(companyEntities);
        return ResponseEntity.of(Optional.of(companyEntitiesModel));
    }

    public ResponseEntity<?> getAllCompanies(){
        List<CellPhoneCompany> companies = repo.findAll();
        return returnEntityList(companies);
    }

    public ResponseEntity<?> getCompanyBySpecificParameter(@NonNull String param, @NonNull String value) {
        List<CellPhoneCompany> companyEntities = this.methodsByParamsMap.get(param).apply(value);
        return returnEntityList(companyEntities);
    }

    public ResponseEntity<?> deleteCompany(String companyName){
        repo.deleteCellPhoneCompanyByCompanyName(companyName);
        return ResponseEntity.ok("Company " + companyName + "has been deleted.");
    }

    public ResponseEntity<?> updateCompany(@NonNull CellPhoneCompany company){
        repo.save(company);
        log.info("Company {} has been updated", company.getCompanyName());
        return ResponseEntity.of(Optional.of(assembler.toModel(company)));
    }

    public ResponseEntity<?> createCompany(@NonNull CellPhoneCompany company){
        repo.save(company);
        log.info("company {} has been created", company.getCompanyName());
        return ResponseEntity.of(Optional.of(assembler.toModel(company)));
    }



}
