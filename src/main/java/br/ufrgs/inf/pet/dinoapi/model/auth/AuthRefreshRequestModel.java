package br.ufrgs.inf.pet.dinoapi.model.auth;

import javax.validation.constraints.NotNull;

public class AuthRefreshRequestModel {
    @NotNull
    private String refreshToken;

    public AuthRefreshRequestModel(){ }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
