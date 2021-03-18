package br.ufrgs.inf.pet.dinoapi.repository.user;

import br.ufrgs.inf.pet.dinoapi.entity.auth.responsible.RecoverPasswordRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecoverPasswordRequestRepository extends CrudRepository<RecoverPasswordRequest, Long>  {
    @Query("SELECT rpr FROM RecoverPasswordRequest rpr WHERE rpr.user.id = :userId")
    List<RecoverPasswordRequest> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT rpr FROM RecoverPasswordRequest rpr WHERE rpr.user.id = :userId ORDER BY rpr.date DESC")
    List<RecoverPasswordRequest> findAllByUserIdOrderByDate(@Param("userId") Long userId);
}
