package br.ufrgs.inf.pet.dinoapi.model.auth.google;

import br.ufrgs.inf.pet.dinoapi.model.auth.AuthResponseModel;

public class GoogleAuthResponseModel extends AuthResponseModel {
    private String googleAccessToken;

    private Long googleExpiresDate;

    public GoogleAuthResponseModel() {}

    public String getGoogleAccessToken() {
        return googleAccessToken;
    }

    public void setGoogleAccessToken(String googleAccessToken) {
        this.googleAccessToken = googleAccessToken;
    }

    public Long getGoogleExpiresDate() {
        return googleExpiresDate;
    }

    public void setGoogleExpiresDate(Long googleExpiresDate) {
        this.googleExpiresDate = googleExpiresDate;
    }
}