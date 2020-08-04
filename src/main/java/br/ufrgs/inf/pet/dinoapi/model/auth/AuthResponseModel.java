package br.ufrgs.inf.pet.dinoapi.model.auth;

import br.ufrgs.inf.pet.dinoapi.model.user.UserResponseModel;

public class AuthResponseModel {

    private String accessToken;

    private UserResponseModel user;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public UserResponseModel getUser() {
        return user;
    }

    public void setUser(UserResponseModel user) {
        this.user = user;
    }
}
