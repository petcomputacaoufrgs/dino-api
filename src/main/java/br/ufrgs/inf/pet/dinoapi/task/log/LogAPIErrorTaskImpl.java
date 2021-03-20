package br.ufrgs.inf.pet.dinoapi.task.log;

import br.ufrgs.inf.pet.dinoapi.configuration.properties.LogConfig;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogUtilsBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class LogAPIErrorTaskImpl extends LogUtilsBase implements LogAPIErrorTask {
    private final LogConfig logConfig;

    @Autowired
    public LogAPIErrorTaskImpl(LogAPIErrorServiceImpl logAPIErrorService, LogConfig logConfig) {
        super(logAPIErrorService);
        this.logConfig = logConfig;
    }

    @Override
    @Async("defaultThreadPoolTaskExecutor")
    @Scheduled(fixedRateString  = "${log.api-log-duration-in-milliseconds}",
            initialDelayString = "${log.api-log-duration-in-milliseconds}")
    public void deleteOldData() {
        try {
            final LocalDateTime deadline = LocalDateTime.now().minusSeconds(logConfig.getAppLogDurationInMilliseconds() / 1000);
            this.logAPIErrorService.deleteOldItems(deadline);
        } catch (Exception e) {
            this.logAPIError(e);
        }
    }
}
