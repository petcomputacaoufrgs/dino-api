package br.ufrgs.inf.pet.dinoapi.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "googleoauth2")
public class GoogleOAuth2Config {
    private String clientid;
    private String clientsecret;
    private String apikey;

    public String getClientsecret() {
        return clientsecret;
    }

    public void setClientsecret(String clientsecret) {
        this.clientsecret = clientsecret;
    }

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public String getAPIkey() {
        return apikey;
    }

    public void setAPIkey(String apikey) {
        this.apikey = apikey;
    }
}
