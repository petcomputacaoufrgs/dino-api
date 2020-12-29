package br.ufrgs.inf.pet.dinoapi.model.auth;

import br.ufrgs.inf.pet.dinoapi.model.user.UserDataModel;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class AuthResponseDataModel {

    private String accessToken;

    private String refreshToken;

    private ZonedDateTime expiresDate;

    private UserDataModel user;

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

    public UserDataModel getUser() {
        return user;
    }

    public void setUser(UserDataModel user) {
        this.user = user;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
