package br.ufrgs.inf.pet.dinoapi.model.auth;

/**
 * Model para envio do token de acesso
 *
 * @author joao.silva
 */
public class GoogleAuthResponseModel {

    String accessToken;

    String googleAccessToken;

    String name;

    String email;

    String pictureUrl;

    public GoogleAuthResponseModel() {}

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getGoogleAccessToken() {
        return googleAccessToken;
    }

    public void setGoogleAccessToken(String googleAccessToken) {
        this.googleAccessToken = googleAccessToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
