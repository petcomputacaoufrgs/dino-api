package br.ufrgs.inf.pet.dinoapi.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "user_app_settings")
public class UserAppSettings implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String SEQUENCE_NAME = "user_app_settings_seq";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Basic(optional = false)
    @Size(min = 1, max = 5, message = "O código de linguagem deve ter entre 1 e 5 caracteres.")
    @Column(name = "language", length = 5)
    private String language;

    @Basic(optional = false)
    @NotNull(message = "Versão não pode ser nula.")
    @Column(name = "version")
    private Long version;

    @OneToOne
    @NotNull
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Long getId() {
        return id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
