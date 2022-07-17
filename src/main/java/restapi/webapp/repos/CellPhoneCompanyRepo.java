package restapi.webapp.repos;

import org.springframework.data.jpa.repository.Modifying;
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

    @Modifying
    @Query(nativeQuery = true,
            value = "DELETE FROM USERS_COMPANIES WHERE COMPANY_ID IN (SELECT CELL_PHONE_COMPANY_ID FROM CELL_PHONE_COMPANIES WHERE COMPANY_NAME = :name)")
    void deleteCellPhoneCompanyFromUserCompaniesTableByCompanyName(@Param("name") String name);

    void deleteCellPhoneCompanyByCompanyName(String companyName);

    void deleteCellPhoneCompanyByCellPhoneCompanyId(@Param("id") Long id);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM USERS_COMPANIES WHERE COMPANY_ID = :id")
    void deleteCellPhoneCompanyFromUserCompaniesTableById(@Param("id")Long id);

    @Query(nativeQuery = true,
            value = "SELECT COUNT(*) FROM USERS P WHERE P.USER_ID = :id)")
    int getUserExists(@Param("id") Long id);

    @Query(nativeQuery = true,
            value = "SELECT * FROM CELL_PHONE_COMPANIES P WHERE P.CELL_PHONE_COMPANY_ID IN (SELECT COMPANY_ID FROM USERS_COMPANIES WHERE USER_ID = :id)")
    List<CellPhoneCompanyEntity> getCellPhoneCompaniesByUserId(@Param("id") Long id);}
