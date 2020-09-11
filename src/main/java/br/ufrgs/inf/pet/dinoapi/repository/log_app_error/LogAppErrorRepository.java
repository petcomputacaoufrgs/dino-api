package br.ufrgs.inf.pet.dinoapi.repository.log_app_error;

import br.ufrgs.inf.pet.dinoapi.entity.log_app_error.LogAppError;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogAppErrorRepository extends CrudRepository<LogAppError, Long> {}