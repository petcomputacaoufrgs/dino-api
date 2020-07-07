package br.ufrgs.inf.pet.dinoapi.entity;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
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

    @Valid
    @OneToMany(mappedBy = "user")
    private List<Auth> auths;

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
