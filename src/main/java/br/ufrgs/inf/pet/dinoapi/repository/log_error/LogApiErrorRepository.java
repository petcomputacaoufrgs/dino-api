package br.ufrgs.inf.pet.dinoapi.repository.log_error;

import br.ufrgs.inf.pet.dinoapi.entity.log_error.LogAPIError;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogApiErrorRepository extends CrudRepository<LogAPIError, Long> { }
