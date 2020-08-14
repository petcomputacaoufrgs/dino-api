package br.ufrgs.inf.pet.dinoapi.model.auth;

import javax.validation.constraints.NotNull;

public class AuthRefreshRequestModel {
    @NotNull
    private String accessToken;

    public AuthRefreshRequestModel(){ }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
