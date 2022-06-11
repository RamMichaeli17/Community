package restapi.webapp.repos;

import org.apache.catalina.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import restapi.webapp.entities.UserEntity;
import restapi.webapp.enums.AvatarGroups;
import restapi.webapp.entities.UserEntity.*;

import java.util.List;

/**
 * This class is a Data Access Layer class (DAL)
 * Basic CRUD functionality (Create,Read,Update,Delete) is
 * implemented according to the specific database
 */
@Repository
public interface UserRepo extends CrudRepository<UserEntity, Long> {
    List<UserEntity> findAll();
    UserEntity getUserEntityByEmail(String email);
    //UserEntity getUserEntityByName(String firstName, String lastName);
    List<UserEntity> getUserEntitiesByAge(Integer age);
}