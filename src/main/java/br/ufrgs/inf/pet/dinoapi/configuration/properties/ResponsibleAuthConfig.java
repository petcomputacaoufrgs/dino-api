package br.ufrgs.inf.pet.dinoapi.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "auth.responsible")
public class ResponsibleAuthConfig {
    private Long delayToClearOldRecoverAttemptsInMilliseconds;
    private Integer recoverCodeLength;
    private Integer maxDelayToRecoverPasswordInMin;

    public Long getDelayToClearOldRecoverAttemptsInMilliseconds() {
        return delayToClearOldRecoverAttemptsInMilliseconds;
    }

    public void setDelayToClearOldRecoverAttemptsInMilliseconds(Long delayToClearOldRecoverAttemptsInMilliseconds) {
        this.delayToClearOldRecoverAttemptsInMilliseconds = delayToClearOldRecoverAttemptsInMilliseconds;
    }

    public Integer getRecoverCodeLength() {
        return recoverCodeLength;
    }

    public void setRecoverCodeLength(Integer recoverCodeLength) {
        this.recoverCodeLength = recoverCodeLength;
    }

    public Integer getMaxDelayToRecoverPasswordInMin() {
        return maxDelayToRecoverPasswordInMin;
    }

    public void setMaxDelayToRecoverPasswordInMin(Integer maxDelayToRecoverPasswordInMin) {
        this.maxDelayToRecoverPasswordInMin = maxDelayToRecoverPasswordInMin;
    }
}
