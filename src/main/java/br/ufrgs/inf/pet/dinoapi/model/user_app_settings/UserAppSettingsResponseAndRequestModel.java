package br.ufrgs.inf.pet.dinoapi.model.user_app_settings;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserAppSettingsResponseAndRequestModel {

    @NotNull(message = "language cannot be null.")
    @Size(max = 5, message = "language should not be more than 5.")
    private String language;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
