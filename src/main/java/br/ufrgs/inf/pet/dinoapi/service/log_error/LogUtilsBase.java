package br.ufrgs.inf.pet.dinoapi.service.log_error;

import br.ufrgs.inf.pet.dinoapi.entity.log_error.LogAPIError;
import java.time.LocalDateTime;

public abstract class LogUtilsBase {
    protected final LogAPIErrorServiceImpl logAPIErrorService;

    public LogUtilsBase(LogAPIErrorServiceImpl logAPIErrorService) {
        this.logAPIErrorService = logAPIErrorService;
    }

    public void logAPIError(String error) {
        final LogAPIError log = new LogAPIError();
        final String className = this.getClass().getSimpleName();
        log.setDate(LocalDateTime.now());
        log.setClassName(className);
        log.setError(error);
        logAPIErrorService.save(log);
    }
}
