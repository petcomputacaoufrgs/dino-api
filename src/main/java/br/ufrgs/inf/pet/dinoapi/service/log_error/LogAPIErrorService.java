package br.ufrgs.inf.pet.dinoapi.service.log_error;

import br.ufrgs.inf.pet.dinoapi.entity.log_error.LogAPIError;
import java.time.LocalDateTime;

public interface LogAPIErrorService {
    /**
     * Save a log on database
     * @param entity Log entity
     */
    void save(LogAPIError entity);

    /**
     * Delete all logs with date before the parameter
     * @param deadline Deadline date for remove logs
     */
    void deleteOldItems(LocalDateTime deadline);
}
