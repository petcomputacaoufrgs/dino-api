package br.ufrgs.inf.pet.dinoapi.repository.user;

import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import br.ufrgs.inf.pet.dinoapi.entity.user.UserSettings;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserSettingsRepository extends CrudRepository<UserSettings, Long>  {
    @Query("SELECT us FROM UserSettings us WHERE us.id = :id AND us.user.id = :userId")
    Optional<UserSettings> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userID);

    @Query("SELECT us FROM UserSettings us WHERE us.user.id = :userId")
    List<UserSettings> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT us FROM UserSettings us WHERE us.id IN :ids AND us.user.id = :userId")
    List<UserSettings> findAllByIdsAndUserId(@Param("ids") List<Long> ids, @Param("userId") Long userID);

    @Query("SELECT us FROM UserSettings us WHERE us.id NOT IN :ids AND us.user.id = :userId")
    List<UserSettings> findAllByUserIdExcludingIds(@Param("userId") Long userID, @Param("ids") List<Long> ids);

    @Query("SELECT us FROM UserSettings us WHERE us.treatment.id = :treatmentId")
    List<UserSettings> findAllByTreatmentId(@Param("treatmentId") Long treatmentId);
}
