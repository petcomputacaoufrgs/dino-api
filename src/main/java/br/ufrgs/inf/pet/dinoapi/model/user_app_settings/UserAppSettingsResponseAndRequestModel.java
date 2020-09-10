package br.ufrgs.inf.pet.dinoapi.model.user_app_settings;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static br.ufrgs.inf.pet.dinoapi.constants.AuthConstants.*;

public class UserAppSettingsResponseAndRequestModel {

    @NotNull(message = LANGUAGE_NULL_MESSAGE)
    @Size(max = LANGUAGE_MAX, message = LANGUAGE_MESSAGE)
    private String language;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
