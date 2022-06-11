package restapi.webapp.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import restapi.webapp.entities.UserEntity;

import java.util.List;

/**
 * This class is a Data Access Layer class (DAL)
 * Basic CRUD functionality (Create,Read,Update,Delete) is
 * implemented according to the specific database
 */
@Repository
public interface UserRepo extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findAll();
    UserEntity getUserEntityByEmail(String email);
    //UserEntity getUserEntityBy(String param, String value);
    void deleteUserEntityByEmail(String email);
}