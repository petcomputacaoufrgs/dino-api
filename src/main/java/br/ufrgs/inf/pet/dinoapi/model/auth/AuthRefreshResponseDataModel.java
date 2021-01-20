package br.ufrgs.inf.pet.dinoapi.model.auth;

import java.time.ZonedDateTime;

public class AuthRefreshResponseDataModel {
    private String accessToken;

    private ZonedDateTime expiresDate;

    public AuthRefreshResponseDataModel() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public ZonedDateTime getExpiresDate() {
        return expiresDate;
    }

    public void setExpiresDate(ZonedDateTime expiresDate) {
        this.expiresDate = expiresDate;
    }
}
