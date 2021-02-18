package br.ufrgs.inf.pet.dinoapi.repository.log_error;

import br.ufrgs.inf.pet.dinoapi.entity.log_error.LogAppError;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Repository
public interface LogAppErrorRepository extends CrudRepository<LogAppError, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM LogAppError l WHERE l.date <= :date")
    void deleteAllByDate(@Param("date") LocalDateTime date);
}