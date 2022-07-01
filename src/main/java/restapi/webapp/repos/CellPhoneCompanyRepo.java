package restapi.webapp.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import restapi.webapp.entities.CellPhoneCompany;

import java.util.List;

/**
 * This class is a Data Access Layer class (DAL)
 * Basic CRUD functionality (Create,Read,Update,Delete) is implemented according to the specific database.
 */
@Repository
public interface CellPhoneCompanyRepo extends CrudRepository<CellPhoneCompany,Long> {
    List<CellPhoneCompany> findAll();
    List<CellPhoneCompany> getCellPhoneCompanyByCellPhoneCompanyId(Long id);
    List<CellPhoneCompany> getCellPhoneCompanyByCompanyName(String name);
    void deleteCellPhoneCompanyByCompanyName(String companyName);
    void deleteCellPhoneCompanyByCellPhoneCompanyId(Long id);
}
