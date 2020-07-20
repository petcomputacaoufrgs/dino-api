package br.ufrgs.inf.pet.dinoapi.entity;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "user_app_settings")
public class UserAppSettings implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String SEQUENCE_NAME = "user_app_settings_seq";

    public final Long DEFAULT_VERSION = 0l;

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Basic(optional = false)
    @Column(name = "language", length = 5)
    private String language;

    @Basic(optional = false)
    @Column(name = "version")
    private Long version;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public UserAppSettings() {}

    public UserAppSettings(User user) {
        this.user = user;
        this.version = this.DEFAULT_VERSION;
    }

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

    public void updateVersion() {
        this.version = version + 1l;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
