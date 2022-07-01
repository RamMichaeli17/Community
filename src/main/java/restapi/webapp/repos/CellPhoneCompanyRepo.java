package restapi.webapp.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import restapi.webapp.entities.CellPhoneCompanyEntity;

import java.util.List;

/**
 * This class is a Data Access Layer class (DAL)
 * Basic CRUD functionality (Create,Read,Update,Delete) is implemented according to the specific database.
 */
@Repository
public interface CellPhoneCompanyRepo extends CrudRepository<CellPhoneCompanyEntity,Long> {
    List<CellPhoneCompanyEntity> findAll();
    List<CellPhoneCompanyEntity> getCellPhoneCompanyByCellPhoneCompanyId(Long id);
    List<CellPhoneCompanyEntity> getCellPhoneCompanyByCompanyName(String name);
    void deleteCellPhoneCompanyByCompanyName(String companyName);
    void deleteCellPhoneCompanyByCellPhoneCompanyId(Long id);
}
