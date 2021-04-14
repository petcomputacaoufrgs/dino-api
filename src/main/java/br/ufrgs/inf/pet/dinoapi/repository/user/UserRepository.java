package br.ufrgs.inf.pet.dinoapi.repository.user;

import br.ufrgs.inf.pet.dinoapi.entity.treatment.Treatment;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.userSettings.includeEssentialContact = true AND u.permission = :permission")
    List<User> findBySaveEssentialContactsAndPermission(@Param("permission") String permission);

    @Query("SELECT u FROM User u WHERE u.userSettings.includeEssentialContact = true AND u.userSettings.treatment IN :treatments AND u.permission = :permission")
    List<User> findBySaveEssentialContactsAndTreatmentsAndPermission(@Param("treatments") List<Treatment> treatments, @Param("permission") String permission);
}
