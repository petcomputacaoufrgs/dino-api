package br.ufrgs.inf.pet.dinoapi.model.auth;

/**
 * Model para recebimento do Token de autorização
 *
 * @author joao.silva
 */
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
