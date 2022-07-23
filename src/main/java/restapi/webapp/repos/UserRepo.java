package restapi.webapp.repos;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import restapi.webapp.entities.UserEntity;

import java.util.List;

/**
 * This class is a Data Access Layer class (DAL)
 * Basic CRUD functionality (Create,Read,Update,Delete) is implemented according to the specific database,
 * in addition to more complex queries.
 */
@Repository
public interface UserRepo extends CrudRepository<UserEntity, Long> {
    @Query(value = "select u from UserEntity u where u.name.first = :first and u.name.last = :last")
    List<UserEntity> getUserEntitiesByName(@Param("first") String first, @Param("last") String last);

    @Query(value = "select u from UserEntity u where u.location.city = :city and " +
            "u.location.street.name = :streetName and u.location.street.number = :streetNumber and u.location.country = :country")
    List<UserEntity> getUserEntitiesByLocation(@Param("city") String city, @Param("streetName") String streetName,
                                               @Param("streetNumber") String streetNumber,
                                               @Param("country") String country);

    @Query(value = "select u from UserEntity u where u.age >= :lower and u.age <= :upper and u.name.last like %:startingChar% ")
    List<UserEntity> getUserEntityByAgeBetweenAndLastNameStartingWith(@Param("lower")Integer lower,
                                                                  @Param("upper")Integer upper,
                                                                  @Param("startingChar") String startingChar);

    @Query(nativeQuery = true,
            value = "SELECT * FROM USERS WHERE USER_ID IN (SELECT USER_ENTITY_USER_ID FROM USER_ENTITY_PHONE_NUMBERS WHERE PHONE_NUMBERS LIKE %:phone%)")
    List<UserEntity> getUserEntityByPhoneNumbersContains(@Param("phone") String phone);

    @Query(nativeQuery = true,
            value = "SELECT * FROM USERS WHERE USER_ID IN (SELECT USER_ID FROM USERS_COMPANIES WHERE COMPANY_ID = :companyId)")
    List<UserEntity> getUserEntitiesByCellPhoneCompanyId(@Param("companyId") Long companyId);

    List<UserEntity> findAll();
    List<UserEntity> getUserEntitiesByGender(String gender);
    List<UserEntity> getUserEntitiesByAge(Integer age);
    List<UserEntity> getUserEntityByUserId(Long userId);
    List<UserEntity> getUserEntityByEmail(String email);
    List<UserEntity> getUserEntityByPhoneNumbers(String phone);
    void deleteUserEntityByUserId(Long userId);
    void deleteUserEntityByEmail(String email);
    @Query(value = "select u from UserEntity u where u.name.first = :first")
    List<UserEntity> getUserEntitiesByFirstName(@Param("first") String first);
}