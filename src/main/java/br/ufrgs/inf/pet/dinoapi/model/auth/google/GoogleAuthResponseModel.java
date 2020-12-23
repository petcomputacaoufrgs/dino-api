package br.ufrgs.inf.pet.dinoapi.model.auth.google;

import br.ufrgs.inf.pet.dinoapi.model.auth.AuthResponseModel;

import java.util.List;

public class GoogleAuthResponseModel extends AuthResponseModel {
    private String googleAccessToken;

    private Long googleExpiresDate;

    private List<GoogleScopeDataModel> scopes;

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

    public List<GoogleScopeDataModel> getScopes() {
        return scopes;
    }

    public void setScopes(List<GoogleScopeDataModel> scopes) {
        this.scopes = scopes;
    }
}
