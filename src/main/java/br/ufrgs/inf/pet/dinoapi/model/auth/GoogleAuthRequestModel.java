package br.ufrgs.inf.pet.dinoapi.model.auth;

public class GoogleAuthRequestModel {
    private String token;

    public GoogleAuthRequestModel(){ }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}