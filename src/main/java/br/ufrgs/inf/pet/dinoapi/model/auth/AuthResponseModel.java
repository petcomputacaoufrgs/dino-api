package br.ufrgs.inf.pet.dinoapi.model.auth;

import br.ufrgs.inf.pet.dinoapi.model.user.UserModel;

public class AuthResponseModel {
    private String accessToken;

    private UserModel user;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
