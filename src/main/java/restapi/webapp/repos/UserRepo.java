package restapi.webapp.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    void deleteUserEntityByEmail(String email);

    @Query(value = "select u from UserEntity u where u.name.firstName = ?1 and u.name.lastName = ?2")
    UserEntity getUserEntityByName(String first, String last);

    @Query(value = "select u from UserEntity u where u.gender = ?1")
    List<UserEntity> getUserEntitiesByGender(String gender);

    @Query(value = "select u from UserEntity u where u.location.street = ?1 and " +
            "u.location.city = ?2 and u.location.state = ?3")
    List<UserEntity> getUserEntitiesByLocation(String street, String city, String state);

    UserEntity getUserEntityByEmail(String email);
    List<UserEntity> getUserEntitiesByAge(Integer age);
    UserEntity getUserEntityByPhone(String phone);
}

//hashmap(String, callable)