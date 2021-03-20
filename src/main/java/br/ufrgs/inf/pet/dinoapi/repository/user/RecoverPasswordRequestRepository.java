package br.ufrgs.inf.pet.dinoapi.repository.user;

import br.ufrgs.inf.pet.dinoapi.entity.auth.responsible.RecoverPasswordRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecoverPasswordRequestRepository extends CrudRepository<RecoverPasswordRequest, Long>  {
    @Query("SELECT rpr FROM RecoverPasswordRequest rpr WHERE rpr.user.id = :userId")
    List<RecoverPasswordRequest> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT rpr FROM RecoverPasswordRequest rpr WHERE rpr.user.id = :userId ORDER BY rpr.date DESC")
    List<RecoverPasswordRequest> findAllByUserIdOrderByDate(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM RecoverPasswordRequest rpr WHERE rpr.date <= :date")
    void deleteAllByDate(@Param("date") LocalDateTime date);
}
