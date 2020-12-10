package br.ufrgs.inf.pet.dinoapi.model.auth;

import br.ufrgs.inf.pet.dinoapi.model.user.UserDataModel;

public class AuthResponseModel {

    private String accessToken;

    private Long expiresDate;

    private UserDataModel user;

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

    public UserDataModel getUser() {
        return user;
    }

    public void setUser(UserDataModel user) {
        this.user = user;
    }
}
