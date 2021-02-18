package br.ufrgs.inf.pet.dinoapi.configuration.application_properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "log")
public class LogConfig {
    private Integer appLogDurationInDays;
    private Integer apiLogDurationInDays;

    public Integer getAppLogDurationInDays() {
        return appLogDurationInDays;
    }

    public void setAppLogDurationInDays(Integer appLogDurationInDays) {
        this.appLogDurationInDays = appLogDurationInDays;
    }

    public Integer getApiLogDurationInDays() {
        return apiLogDurationInDays;
    }

    public void setApiLogDurationInDays(Integer apiLogDurationInDays) {
        this.apiLogDurationInDays = apiLogDurationInDays;
    }
}
