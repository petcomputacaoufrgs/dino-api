package br.ufrgs.inf.pet.dinoapi.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppConfig {
    private String origin;

    private String adminEmail;

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getOrigin() {
        return origin;
    }
}
