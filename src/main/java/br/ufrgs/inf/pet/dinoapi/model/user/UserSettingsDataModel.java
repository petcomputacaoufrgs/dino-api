package br.ufrgs.inf.pet.dinoapi.model.user;

import br.ufrgs.inf.pet.dinoapi.constants.UserSettingsConstants;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataLocalIdModel;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserSettingsDataModel extends SynchronizableDataLocalIdModel<Long> {
    @NotNull(message = UserSettingsConstants.LANGUAGE_NULL_MESSAGE)
    private Integer language;

    @NotNull(message = UserSettingsConstants.COLOR_THEME__NULL_MESSAGE)
    private Integer colorTheme;

    @NotNull(message = UserSettingsConstants.FONT_SIZE__NULL_MESSAGE)
    private Integer fontSize;

    private Long treatmentId;

    @NotNull(message = UserSettingsConstants.INCLUDE_ESSENTIAL_CONTACT_NULL_MESSAGE)
    private Boolean includeEssentialContact;

    @NotNull(message = UserSettingsConstants.DECLINE_GOOGLE_CONTACTS_NULL_MESSAGE)
    private Boolean declineGoogleContacts;

    @NotNull(message = UserSettingsConstants.FIRST_SETTINGS_DONE_NULL_MESSAGE)
    private Boolean firstSettingsDone;

    @Size(min = UserSettingsConstants.PASSWORD_MIN, max = UserSettingsConstants.PASSWORD_MAX, message = UserSettingsConstants.PASSWORD_SIZE_MESSAGE)
    private String parentsAreaPassword;

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }

    public Integer getColorTheme() {
        return colorTheme;
    }

    public void setColorTheme(Integer colorTheme) {
        this.colorTheme = colorTheme;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public Long getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(Long treatmentId) {
        this.treatmentId = treatmentId;
    }

    public Boolean getIncludeEssentialContact() {
        return includeEssentialContact;
    }

    public void setIncludeEssentialContact(Boolean includeEssentialContact) {
        this.includeEssentialContact = includeEssentialContact;
    }

    public Boolean getDeclineGoogleContacts() {
        return declineGoogleContacts;
    }

    public void setDeclineGoogleContacts(Boolean declineGoogleContacts) {
        this.declineGoogleContacts = declineGoogleContacts;
    }

    public Boolean getFirstSettingsDone() {
        return firstSettingsDone;
    }

    public void setFirstSettingsDone(Boolean firstSettingsDone) {
        this.firstSettingsDone = firstSettingsDone;
    }

    public String getParentsAreaPassword() {
        return parentsAreaPassword;
    }

    public void setParentsAreaPassword(String parentsAreaPassword) {
        this.parentsAreaPassword = parentsAreaPassword;
    }
}
