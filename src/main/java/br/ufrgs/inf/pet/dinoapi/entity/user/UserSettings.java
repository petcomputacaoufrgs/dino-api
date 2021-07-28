package br.ufrgs.inf.pet.dinoapi.entity.user;

import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.entity.treatment.Treatment;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import javax.persistence.*;

@Entity
@Table(name = "user_settings")
public class UserSettings extends SynchronizableEntity<Long> {
    @Column(name = "language")
    private Integer language;

    @Column(name = "color_theme", nullable = false)
    private Integer colorTheme;

    @Column(name = "font_size", nullable = false)
    private Integer fontSize;

    @Column(name = "include_essential_contacts", nullable = false)
    private Boolean includeEssentialContact;

    @Column(name = "decline_google_contacts", nullable = false)
    private Boolean declineGoogleContacts;

    @Column(name = "first_settings_done", nullable = false)
    private Boolean firstSettingsDone;

    @Column(name = "settings_step", nullable = false)
    private Integer step;

    @Column(name = "parents_area_password", length = PASSWORD_MAX)
    private String parentsAreaPassword;

    @Column(name = "sync_google_contacts", nullable = false)
    private Boolean shouldSyncGoogleContacts;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne
    @JoinColumn(name = "treatment_id")
    private Treatment treatment;

    public UserSettings() { }

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

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public boolean shouldSyncGoogleContacts() {
        return shouldSyncGoogleContacts;
    }

    public void setShouldSyncGoogleContacts(boolean syncGoogleContacts) {
        this.shouldSyncGoogleContacts = syncGoogleContacts;
    }
}
