package br.ufrgs.inf.pet.dinoapi.entity;

import br.ufrgs.inf.pet.dinoapi.entity.contacts.*;
import br.ufrgs.inf.pet.dinoapi.entity.note.Note;
import br.ufrgs.inf.pet.dinoapi.entity.note.NoteColumn;
import br.ufrgs.inf.pet.dinoapi.entity.note.NoteColumnVersion;
import br.ufrgs.inf.pet.dinoapi.entity.note.NoteVersion;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "dino_user")
public class User {
    private static final String SEQUENCE_NAME = "dino_user_seq";

    public final Long DEFAULT_VERSION = 0l;

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "email", length = 100, unique = true, nullable = false)
    private String email;

    @Column(name = "picture_url", length = 500, nullable = false)
    private String pictureURL;

    @Column(name = "version", nullable = false)
    private Long version;

    @OneToMany(mappedBy = "user")
    private List<Auth> auths;

    @OneToOne(mappedBy = "user")
    private GoogleAuth googleAuth;

    @OneToOne(mappedBy = "user")
    private UserAppSettings userAppSettings;

    @OneToOne(mappedBy = "user")
    private NoteVersion noteVersion;

    @OneToOne(mappedBy = "user")
    private NoteColumnVersion noteColumnVersion;

    @OneToMany(mappedBy = "user")
    private List<Note> notes;

    @OneToMany(mappedBy = "user")
    private List<NoteColumn> noteColumns;

    @OneToMany(mappedBy = "user")
    private List<Contact> contacts;

    @OneToOne(mappedBy = "user")
    private ContactVersion contactVersion;
    
    public User() {
        this.notes = new ArrayList<>();
        this.auths = new ArrayList<>();
        this.contacts = new ArrayList<>();
    }

    public User(String name, String email, String pictureURL) {
        this.name = name;
        this.email = email;
        this.pictureURL = pictureURL;
        this.version = this.DEFAULT_VERSION;
        this.notes = new ArrayList<>();
        this.auths = new ArrayList<>();
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

    public List<NoteColumn> getNoteColumns() {
        return noteColumns;
    }

    public void setNoteColumns(List<NoteColumn> noteColumns) {
        this.noteColumns = noteColumns;
    }

    public NoteVersion getNoteVersion() {
        return noteVersion;
    }

    public NoteColumnVersion getNoteColumnVersion() {
        return noteColumnVersion;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public List<Contact> getContacts() { return contacts; }

    public ContactVersion getContactVersion() {
        return contactVersion;
    }

    public void setContactVersion(ContactVersion contactVersion) {
        this.contactVersion = contactVersion;
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
