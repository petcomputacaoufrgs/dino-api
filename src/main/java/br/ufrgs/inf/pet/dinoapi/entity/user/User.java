package br.ufrgs.inf.pet.dinoapi.entity.user;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.auth.google.GoogleAuth;
import br.ufrgs.inf.pet.dinoapi.entity.calendar.Event;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.entity.kids_space.KidsSpaceSettings;
import br.ufrgs.inf.pet.dinoapi.entity.note.NoteColumn;
import br.ufrgs.inf.pet.dinoapi.entity.report.Report;
import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.entity.treatment.TreatmentQuestion;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import static br.ufrgs.inf.pet.dinoapi.constants.UserConstants.*;

@Entity
@Table(name = "dino_user")
public class User extends SynchronizableEntity<Long> {
    @Column(name = "name", length = NAME_MAX, nullable = false)
    private String name;

    @Column(name = "email", length = EMAIL_MAX, unique = true, nullable = false)
    private String email;

    @Column(name = "picture_url", length = PICTURE_URL_MAX, nullable = false)
    private String pictureURL;

    @Column(name = "permission", length = PERMISSION_MAX, nullable = false)
    private String permission;

    @OneToOne(mappedBy = "user")
    private GoogleAuth googleAuth;

    @OneToOne(mappedBy = "user")
    private UserSettings userSettings;

    @OneToOne(mappedBy = "user")
    private KidsSpaceSettings kidsSpaceSettings;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Auth> auths;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<NoteColumn> noteColumns;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Contact> contacts;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Event> events;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<TreatmentQuestion> treatmentQuestions;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Report> reports;

    public User() { }

    public User(String name, String email, String pictureURL) {
        this.name = name;
        this.email = email;
        this.pictureURL = pictureURL;
        this.lastUpdate = LocalDateTime.now();
    }

    public boolean hasGoogleAuth() {
        return googleAuth != null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public GoogleAuth getGoogleAuth() {
        return googleAuth;
    }

    public void setGoogleAuth(GoogleAuth googleAuth) {
        this.googleAuth = googleAuth;
    }

    public UserSettings getUserAppSettings() {
        return userSettings;
    }

    public void setUserAppSettings(UserSettings userSettings) {
        this.userSettings = userSettings;
    }

    public KidsSpaceSettings getKidsSpaceSettings() {
        return kidsSpaceSettings;
    }

    public void setKidsSpaceSettings(KidsSpaceSettings kidsSpaceSettings) {
        this.kidsSpaceSettings = kidsSpaceSettings;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public List<Contact> getContacts() { return contacts; }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
