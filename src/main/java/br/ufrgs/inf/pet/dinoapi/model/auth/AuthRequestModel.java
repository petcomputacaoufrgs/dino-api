package br.ufrgs.inf.pet.dinoapi.model.auth;

/**
 * Model para recebimento do Token de autorização
 *
 * @author joao.silva
 */
public class AuthRequestModel {
    private String token;

    public AuthRequestModel(){ }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
