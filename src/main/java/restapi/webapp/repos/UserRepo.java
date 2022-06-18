package restapi.webapp.repos;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
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
    @Query(value = "select u from UserEntity u where u.name.first = :first and u.name.last = :last")
    List<UserEntity> getUserEntitiesByName(@Param("first") String first, @Param("last") String last);

    @Query(value = "select u from UserEntity u where u.location.city = :city and " +
            "u.location.street.name = :streetName and u.location.street.number = :streetNumber and u.location.country = :country")
    List<UserEntity> getUserEntitiesByLocation(@Param("city") String city, @Param("streetName") String streetName,
                                               @Param("streetNumber") String streetNumber,
                                               @Param("country") String country);
    List<UserEntity> getUserEntitiesByGender(String gender);
    List<UserEntity> findAll();
    List<UserEntity> getUserEntitiesByAge(Integer age);
    List<UserEntity> getUserEntityById(Long id);
    List<UserEntity> getUserEntityByEmail(String email);
    List<UserEntity> getUserEntityByPhone(String phone);
    List<UserEntity> getUserEntityByPhoneContains(String phone);
    void deleteUserEntityById(Long id);
}