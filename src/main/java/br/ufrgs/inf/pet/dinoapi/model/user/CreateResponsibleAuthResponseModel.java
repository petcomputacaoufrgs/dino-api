package br.ufrgs.inf.pet.dinoapi.model.user;

public class CreateResponsibleAuthResponseModel {
    private Boolean success;
    private String token;
    private String iv;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }
}
