package br.ufrgs.inf.pet.dinoapi.entity.user;

import br.ufrgs.inf.pet.dinoapi.constants.UserSettingsConstants;
import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.entity.treatment.Treatment;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "user_settings")
public class UserSettings extends SynchronizableEntity<Long>  {

    @Column(name = "language", length = UserSettingsConstants.LANGUAGE_MAX, nullable = false)
    private String language;

    @Column(name = "color_theme", nullable = false)
    private Integer colorTheme;

    @Column(name = "font_size", nullable = false)
    private Integer fontSize;

    @Column(name = "include_essential_contacts", nullable = false)
    private Boolean includeEssentialContact;

    @Column(name = "sync_google_contacts")
    private Boolean syncGoogleContacts;

    @Column(name = "decline_google_contacts")
    private Boolean declineGoogleContacts;

    @Column(name = "first_settings_done")
    private Boolean firstSettingsDone;

    @Column(name = "settings_step")
    private Integer settingsStep;

    @ManyToOne
    @JoinColumn(name = "treatment_id")
    private Treatment treatment;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public UserSettings() {}

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

    public Integer getFontSize() {
        return fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public Treatment getTreatment() {
        return treatment;
    }

    public void setTreatment(Treatment treatment) {
        this.treatment = treatment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getIncludeEssentialContact() {
        return includeEssentialContact;
    }

    public void setIncludeEssentialContact(Boolean includeEssentialContact) {
        this.includeEssentialContact = includeEssentialContact;
    }

    public Boolean getSyncGoogleContacts() {
        return syncGoogleContacts;
    }

    public void setSyncGoogleContacts(Boolean syncGoogleContacts) {
        this.syncGoogleContacts = syncGoogleContacts;
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

    public Integer getSettingsStep() {
        return settingsStep;
    }

    public void setSettingsStep(Integer settingsStep) {
        this.settingsStep = settingsStep;
    }
}
