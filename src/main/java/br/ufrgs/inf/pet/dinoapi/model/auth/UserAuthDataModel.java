package br.ufrgs.inf.pet.dinoapi.model.auth;

import javax.validation.constraints.NotNull;

public class UserAuthDataModel {

    @NotNull
    private String email;

    @NotNull
    private int authorization;

    public UserAuthDataModel() {}

    public UserAuthDataModel(String email, int authorization) {
        this.email = email;
        this.authorization = authorization;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAuthorization(int authorization) {
        this.authorization = authorization;
    }

    public int getAuthorization() {
        return authorization;
    }
}
