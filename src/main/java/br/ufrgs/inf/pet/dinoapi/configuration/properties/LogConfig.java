package br.ufrgs.inf.pet.dinoapi.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "log")
public class LogConfig {
    private Long appLogDurationInMilliseconds;
    private Long apiLogDurationInMilliseconds;

    public Long getAppLogDurationInMilliseconds() {
        return appLogDurationInMilliseconds;
    }

    public void setAppLogDurationInMilliseconds(Long appLogDurationInMilliseconds) {
        this.appLogDurationInMilliseconds = appLogDurationInMilliseconds;
    }

    public Long getApiLogDurationInMilliseconds() {
        return apiLogDurationInMilliseconds;
    }

    public void setApiLogDurationInMilliseconds(Long apiLogDurationInMilliseconds) {
        this.apiLogDurationInMilliseconds = apiLogDurationInMilliseconds;
    }
}
