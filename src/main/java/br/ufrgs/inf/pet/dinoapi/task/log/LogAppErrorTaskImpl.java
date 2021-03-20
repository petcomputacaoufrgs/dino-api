package br.ufrgs.inf.pet.dinoapi.task.log;

import br.ufrgs.inf.pet.dinoapi.configuration.properties.LogConfig;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAppErrorService;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAppErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogUtilsBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class LogAppErrorTaskImpl extends LogUtilsBase implements LogAppErrorTask {
    private final LogConfig logConfig;
    private final LogAppErrorService logAppErrorService;

    @Autowired
    public LogAppErrorTaskImpl(LogAPIErrorServiceImpl logAPIErrorService, LogConfig logConfig, LogAppErrorServiceImpl logAppErrorService) {
        super(logAPIErrorService);
        this.logConfig = logConfig;
        this.logAppErrorService = logAppErrorService;
    }

    @Override
    @Async("defaultThreadPoolTaskExecutor")
    @Scheduled(fixedRateString = "${log.app-log-duration-in-milliseconds}",
            initialDelayString = "${log.app-log-duration-in-milliseconds}")
    public void deleteOldData() {
        try {
            final LocalDateTime deadline = LocalDateTime.now().minusSeconds(logConfig.getAppLogDurationInMilliseconds() / 1000);
            this.logAppErrorService.deleteOldItems(deadline);
        } catch (Exception e) {
            this.logAPIError(e);
        }
    }
}
