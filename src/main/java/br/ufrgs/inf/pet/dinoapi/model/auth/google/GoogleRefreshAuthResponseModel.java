package br.ufrgs.inf.pet.dinoapi.model.auth.google;

public class GoogleRefreshAuthResponseModel {

    private String googleAccessToken;

    private Long googleExpiresDate;

    public GoogleRefreshAuthResponseModel() {}

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
