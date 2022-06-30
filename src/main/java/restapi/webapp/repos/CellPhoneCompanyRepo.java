package restapi.webapp.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import restapi.webapp.entities.CellPhoneCompany;

@Repository
public interface CellPhoneCompanyRepo extends CrudRepository<CellPhoneCompany,Long> {

}
