package restapi.webapp.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import restapi.webapp.entities.User;

/**
 * This class is a Data Access Layer class (DAL)
 * Basic CRUD functionality (Create,Read,Update,Delete) is
 * implemented according to the specific data base
 */
@Repository
public interface UserRepo extends CrudRepository<User, Long> {
}