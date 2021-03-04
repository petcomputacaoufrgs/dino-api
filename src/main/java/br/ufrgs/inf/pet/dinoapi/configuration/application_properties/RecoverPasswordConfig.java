package br.ufrgs.inf.pet.dinoapi.configuration.application_properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "recover.password.config")
public class RecoverPasswordConfig {
    private Integer codeLength;

    public Integer getCodeLength() {
        return codeLength;
    }

    public void setCodeLength(Integer codeLength) {
        this.codeLength = codeLength;
    }
}
