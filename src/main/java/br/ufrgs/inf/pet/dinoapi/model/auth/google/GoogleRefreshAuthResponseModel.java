package br.ufrgs.inf.pet.dinoapi.model.auth.google;

import java.util.List;

public class GoogleRefreshAuthResponseModel {
    private String googleAccessToken;

    private Long googleExpiresDate;

    private List<GoogleScopeDataModel> scopes;

    private boolean declinedContactsGrant;

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

    public List<GoogleScopeDataModel> getScopes() {
        return scopes;
    }

    public void setScopes(List<GoogleScopeDataModel> scopes) {
        this.scopes = scopes;
    }

    public boolean isDeclinedContactsGrant() {
        return declinedContactsGrant;
    }

    public void setDeclinedContactsGrant(boolean declinedContactsGrant) {
        this.declinedContactsGrant = declinedContactsGrant;
    }
}
