package br.ufrgs.inf.pet.dinoapi.model.user_app_settings;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static br.ufrgs.inf.pet.dinoapi.constants.AuthConstants.*;

public class UserAppSettingsResponseAndRequestModel {

    @NotNull(message = LANGUAGE_NULL_MESSAGE)
    @Size(max = LANGUAGE_MAX, message = LANGUAGE_MESSAGE)
    private String language;

    @NotNull(message = COLOR_THEME__NULL_MESSAGE)
    private Integer colorTheme;

    @NotNull(message = E_CONTACTS_GRANT__NULL_MESSAGE)
    private boolean loadEssentialContactsGrant;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getColorTheme() {
        return colorTheme;
    }

    public void setColorTheme(Integer colorTheme) {
        this.colorTheme = colorTheme;
    }

    public boolean getLoadEssentialContactsGrant() {
        return loadEssentialContactsGrant;
    }

    public void setLoadEssentialContactsGrant(boolean loadEssentialContactsGrant) {
        this.loadEssentialContactsGrant = loadEssentialContactsGrant;
    }
}
