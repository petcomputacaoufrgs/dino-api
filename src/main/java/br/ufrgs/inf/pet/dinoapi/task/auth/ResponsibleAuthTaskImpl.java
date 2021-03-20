package br.ufrgs.inf.pet.dinoapi.task.auth;

import br.ufrgs.inf.pet.dinoapi.configuration.properties.ResponsibleAuthConfig;
import br.ufrgs.inf.pet.dinoapi.service.auth.ResponsibleAuthServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogAPIErrorServiceImpl;
import br.ufrgs.inf.pet.dinoapi.service.log_error.LogUtilsBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class ResponsibleAuthTaskImpl extends LogUtilsBase implements ResponsibleAuthTask {
    private final ResponsibleAuthConfig responsibleAuthConfig;
    private final ResponsibleAuthServiceImpl responsibleAuthService;

    @Autowired
    public ResponsibleAuthTaskImpl(LogAPIErrorServiceImpl logAPIErrorService, ResponsibleAuthConfig responsibleAuthConfig,
                                   ResponsibleAuthServiceImpl responsibleAuthService) {
        super(logAPIErrorService);
        this.responsibleAuthConfig = responsibleAuthConfig;
        this.responsibleAuthService = responsibleAuthService;
    }

    @Override
    @Async("defaultThreadPoolTaskExecutor")
    @Scheduled(fixedRateString  = "${auth.responsible.delay-to-clear-old-recover-attempts-in-milliseconds}",
            initialDelayString = "${auth.responsible.delay-to-clear-old-recover-attempts-in-milliseconds}")
    public void deleteOldData() {
        try {
            final LocalDateTime deadline = LocalDateTime.now()
                    .minusSeconds(responsibleAuthConfig.getDelayToClearOldRecoverAttemptsInMilliseconds() / 1000);
            this.responsibleAuthService.deleteOldData(deadline);
        } catch (Exception e) {
            this.logAPIError(e);
        }
    }
}
