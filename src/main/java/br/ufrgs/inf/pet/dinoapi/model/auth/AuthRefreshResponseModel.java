package br.ufrgs.inf.pet.dinoapi.model.auth;

public class AuthRefreshResponseModel {
    private String accessToken;

    public AuthRefreshResponseModel(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
