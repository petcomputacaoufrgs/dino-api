package br.ufrgs.inf.pet.dinoapi.entity.user;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;
import br.ufrgs.inf.pet.dinoapi.entity.auth.google.GoogleAuth;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.Contact;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.ContactVersion;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.GoogleContact;
import br.ufrgs.inf.pet.dinoapi.entity.faq.FaqUser;
import br.ufrgs.inf.pet.dinoapi.entity.faq.FaqUserQuestion;
import br.ufrgs.inf.pet.dinoapi.entity.note.NoteColumn;
import br.ufrgs.inf.pet.dinoapi.entity.teste.TesteEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static br.ufrgs.inf.pet.dinoapi.constants.AuthConstants.*;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "dino_user")
public class User {
    private static final String SEQUENCE_NAME = "dino_user_seq";

    public static final Long DEFAULT_VERSION = 0L;

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", length = NAME_MAX, nullable = false)
    private String name;

    @Column(name = "email", length = EMAIL_MAX, unique = true, nullable = false)
    private String email;

    @Column(name = "picture_url", length = PICTURE_URL_MAX, nullable = false)
    private String pictureURL;

    @Column(name = "version", nullable = false)
    private Long version;

    @OneToOne(mappedBy = "user")
    private GoogleAuth googleAuth;

    @OneToOne(mappedBy = "user")
    private UserAppSettings userAppSettings;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Auth> auths;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<NoteColumn> noteColumns;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Contact> contacts;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<TesteEntity> testeEntities;

    @OneToMany(mappedBy = "user")
    private List<GoogleContact> googleContacts;

    @OneToOne(mappedBy = "user")
    private ContactVersion contactVersion;

    @OneToOne(mappedBy = "user")
    private FaqUser faqUser;

    @OneToMany(mappedBy = "user")
    private List<FaqUserQuestion> faqFaqUserQuestions;
    
    public User() {
        this.auths = new ArrayList<>();
        this.contacts = new ArrayList<>();
        this.noteColumns = new ArrayList<>();
    }

    public User(String name, String email, String pictureURL) {
        this.name = name;
        this.email = email;
        this.pictureURL = pictureURL;
        this.version = DEFAULT_VERSION;
        this.auths = new ArrayList<>();
        this.contacts = new ArrayList<>();
        this.noteColumns = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<NoteColumn> getNoteColumns() {
        return noteColumns;
    }

    public void setNoteColumns(List<NoteColumn> noteColumns) {
        this.noteColumns = noteColumns;
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

    public FaqUser getFaqUser() {
        return faqUser;
    }

    public void setFaqUser(FaqUser faqUser) {
        this.faqUser = faqUser;
    }

    public List<GoogleContact> getGoogleContacts() {
        return googleContacts;
    }

    public void setGoogleContacts(List<GoogleContact> googleContacts) {
        this.googleContacts = googleContacts;
    }
}
