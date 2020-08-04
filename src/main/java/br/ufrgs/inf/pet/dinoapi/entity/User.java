package br.ufrgs.inf.pet.dinoapi.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "dino_user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String SEQUENCE_NAME = "dino_user_seq";

    public final Long DEFAULT_VERSION = 0l;

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Basic(optional = false)
    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "email", length = 100, unique = true)
    private String email;

    @Column(name = "picture_url", length = 100, unique = false)
    private String pictureURL;

    @Basic(optional = false)
    @Column(name = "version")
    private Long version;

    @OneToMany(mappedBy = "user")
    private List<Auth> auths;

    @OneToOne(mappedBy = "user")
    private GoogleAuth googleAuth;

    @OneToOne(mappedBy = "user")
    private UserAppSettings userAppSettings;

    @OneToOne(mappedBy = "user")
    private NoteVersion noteVersion;

    @OneToMany(mappedBy = "user")
    private List<Note> notes;

    public User() {}

    public User(String name, String email, String pictureURL) {
        this.name = name;
        this.email = email;
        this.pictureURL = pictureURL;
        this.version = this.DEFAULT_VERSION;
    }

    public Long getId() {
        return id;
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

    public Long getVersion() {
        return version;
    }

    public void updateVersion() {
        this.version = version + 1l;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public GoogleAuth getGoogleAuth() {
        return googleAuth;
    }

    public void setGoogleAuth(GoogleAuth googleAuth) {
        this.googleAuth = googleAuth;
    }

    public UserAppSettings getUserAppSettings() {
        return userAppSettings;
    }

    public void setUserAppSettings(UserAppSettings userAppSettings) {
        this.userAppSettings = userAppSettings;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public NoteVersion getNoteVersion() {
        return noteVersion;
    }

    public void setNoteVersion(NoteVersion noteVersion) {
        this.noteVersion = noteVersion;
    }

    public Boolean tokenIsValid(String token) {
        boolean isValid = false;

        for (Auth auth : auths) {
            if (auth.getAccessToken().equals(token) && auth.tokenIsValid()) {
                isValid = true;
            }
        }

        return isValid;
    }
}
