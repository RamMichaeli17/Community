package restapi.webapp.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import restapi.webapp.entities.CellPhoneCompany;

import java.util.List;

@Repository
public interface CellPhoneCompanyRepo extends CrudRepository<CellPhoneCompany,Long> {
    List<CellPhoneCompany> findAll();
    List<CellPhoneCompany> getCellPhoneCompanyByCellPhoneCompanyId(Long id);
    List<CellPhoneCompany> getCellPhoneCompanyByCompanyName(String name);
    void deleteCellPhoneCompanyByCompanyName(String companyName);
    void deleteCellPhoneCompanyByCellPhoneCompanyId(Long id);
}
