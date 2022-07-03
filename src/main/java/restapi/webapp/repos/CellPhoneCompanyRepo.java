package restapi.webapp.repos;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
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
    CellPhoneCompanyEntity getCellPhoneCompanyByCellPhoneCompanyId(Long id);
    CellPhoneCompanyEntity getCellPhoneCompanyByCompanyName(String name);
    void deleteCellPhoneCompanyByCompanyName(String companyName);
    void deleteCellPhoneCompanyByCellPhoneCompanyId(Long id);
    @Query(nativeQuery = true,
            value = "SELECT * FROM CELL_PHONE_COMPANY P WHERE P.CELL_PHONE_COMPANY_ID IN (SELECT COMPANY_ID FROM USERS_COMPANIES WHERE USER_ID= ?1)")
    List<CellPhoneCompanyEntity> getCellPhoneCompaniesByUserId(@Param("id") Long id);
}
