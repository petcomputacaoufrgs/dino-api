package br.ufrgs.inf.pet.dinoapi.model.auth;

import java.util.Date;

public class AuthRefreshResponseModel {
    private String accessToken;

    private Long expiresDate;

    public AuthRefreshResponseModel(String accessToken, Date expiresDate) {
        this.accessToken = accessToken;
        this.expiresDate = expiresDate.getTime();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getExpiresDate() {
        return expiresDate;
    }

    public void setExpiresDate(Long expiresDate) {
        this.expiresDate = expiresDate;
    }
}
