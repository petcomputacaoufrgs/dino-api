package br.ufrgs.inf.pet.dinoapi.model.auth.google.auth;

import javax.validation.constraints.NotNull;

public class GoogleAuthRequestModel {
    @NotNull
    private String code;

    public GoogleAuthRequestModel() {}

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
