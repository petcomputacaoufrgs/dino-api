package br.ufrgs.inf.pet.dinoapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.util.Date;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * Classe de persistencia para a tabela de dados de autenticação do Google
 *
 * @author joao.silva
 */
@Entity
@Table(name = "google_auth")
public class GoogleAuth {
    private static final long serialVersionUID = 1L;

    private static final String SEQUENCE_NAME = "google_auth_seq";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Column(name = "google_id", length = 100, unique = true)
    private String googleId;

    @Column(name = "access_token", length = 200, unique = true)
    private String accessToken;

    @Column(name = "refresh_token", length = 200, unique = true)
    private String refreshToken;

    @Column(name = "token_expires_data_in_millis")
    private Long tokenExpiresDateInMillis;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public GoogleAuth() {}

    public GoogleAuth(String googleId, String refreshToken) {
        this.googleId = googleId;
        this.refreshToken = refreshToken;
    }

    public Long getTokenExpiresDateInMillis() {
        return tokenExpiresDateInMillis;
    }

    public void setTokenExpiresDateInMillis(Long tokenExpiresDateInMillis) {
        this.tokenExpiresDateInMillis = tokenExpiresDateInMillis;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean tokenIsValid() {
        return (new Date()).getTime() <= this.tokenExpiresDateInMillis;
    }
}
