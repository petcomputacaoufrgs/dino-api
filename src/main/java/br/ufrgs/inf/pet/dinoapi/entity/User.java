package br.ufrgs.inf.pet.dinoapi.entity;

import br.ufrgs.inf.pet.dinoapi.entity.*;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "dino_user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String SEQUENCE_NAME = "dino_user_seq";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Basic(optional = false)
    @NotNull(message = "Nome não pode ser nulo.")
    @Size(min = 1, max = 100, message = "O nome deve conter entre 1 e 100 caracteres.")
    @Column(name = "name", length = 100)
    private String name;

    @Size(min = 1, max = 100, message = "O email deve conter entre 1 e 100 caracteres.")
    @Email(message = "Email inválido.")
    @Column(name = "email", length = 100, unique = false)
    private String email;

    @Size(min = 1, max = 260, message = "O token de acesso deve conter entre 1 e 260 caracteres.")
    @Column(name = "access_token", length = 260, unique = true)
    private String accessToken;

    @Column(name = "token_expires_data_in_millis")
    private Long tokenExpiresDateInMillis;

    @Valid
    @OneToOne(mappedBy = "user")
    private GoogleAuth googleAuth;

    @Valid
    @OneToOne(mappedBy = "user")
    private UserAppSettings userAppSettings;

    @Valid
    @OneToOne(mappedBy = "user")
    private NoteVersion noteVersion;

    @Valid
    @OneToMany(mappedBy = "user")
    private List<Note> notes;

    @Valid
    @OneToMany(mappedBy = "user")
    private List<Contact> contacts;

    public User() {}

    public User(String name, String email) {
        this.name = name;
        this.email = email;
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

    public GoogleAuth getGoogleAuth() {
        return googleAuth;
    }

    public void setGoogleAuth(GoogleAuth googleAuth) {
        this.googleAuth = googleAuth;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getTokenExpiresDateInMillis() {
        return tokenExpiresDateInMillis;
    }

    public void setTokenExpiresDateInMillis(Long tokenExpiresDateInMillis) {
        this.tokenExpiresDateInMillis = tokenExpiresDateInMillis;
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

    public List<Contact> getContacts() { return contacts; }

    public void setNoteVersion(NoteVersion noteVersion) {
        this.noteVersion = noteVersion;
    }

    public Boolean tokenIsValid() {
        return (new Date()).getTime() <= this.tokenExpiresDateInMillis;
    }

}
