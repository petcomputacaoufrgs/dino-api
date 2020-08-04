package br.ufrgs.inf.pet.dinoapi.model.auth.google;

import javax.validation.constraints.NotNull;

public class GoogleAuthRequestModel {

    @NotNull
    private String token;

    public GoogleAuthRequestModel(){ }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
