package br.ufrgs.inf.pet.dinoapi.entity.user;

import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.entity.treatment.Treatment;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import javax.persistence.*;
import static br.ufrgs.inf.pet.dinoapi.constants.UserSettingsConstants.PASSWORD_MAX;

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

    @Column(name = "decline_google_calendar", nullable = false)
    private Boolean declineGoogleCalendar;

    @Column(name = "first_settings_done", nullable = false)
    private Boolean firstSettingsDone;

    @Column(name = "parents_area_password", length = PASSWORD_MAX)
    private String parentsAreaPassword;

    @Column(name = "sync_google_contacts", nullable = false)
    private Boolean shouldSyncGoogleContacts;

    @Column(name = "google_calendar_id")
    private String googleCalendarId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne
    @JoinColumn(name = "treatment_id")
    private Treatment treatment;

    public UserSettings() { }

    public String getGoogleCalendarId() {
        return googleCalendarId;
    }

    public void setGoogleCalendarId(String googleCalendarId) {
        this.googleCalendarId = googleCalendarId;
    }

    public void setDeclineGoogleCalendar(Boolean declineGoogleCalendar) {
        this.declineGoogleCalendar = declineGoogleCalendar;
    }

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

    public Boolean getDeclineGoogleCalendar() {
        return declineGoogleContacts;
    }

    public Boolean getFirstSettingsDone() {
        return firstSettingsDone;
    }

    public void setFirstSettingsDone(Boolean firstSettingsDone) {
        this.firstSettingsDone = firstSettingsDone;
    }

    public boolean shouldSyncGoogleContacts() {
        return shouldSyncGoogleContacts;
    }

    public void setShouldSyncGoogleContacts(boolean syncGoogleContacts) {
        this.shouldSyncGoogleContacts = syncGoogleContacts;
    }

    public String getParentsAreaPassword() {
        return parentsAreaPassword;
    }

    public void setParentsAreaPassword(String parentsAreaPassword) {
        this.parentsAreaPassword = parentsAreaPassword;
    }
}
