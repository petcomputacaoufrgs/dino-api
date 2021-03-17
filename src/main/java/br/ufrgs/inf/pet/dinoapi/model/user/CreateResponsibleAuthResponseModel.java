package br.ufrgs.inf.pet.dinoapi.model.user;

public class CreateResponsibleAuthResponseModel {
    private Boolean success;
    private String hash;
    private String salt;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
