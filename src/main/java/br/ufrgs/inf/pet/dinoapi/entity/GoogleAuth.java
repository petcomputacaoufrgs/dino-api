package br.ufrgs.inf.pet.dinoapi.entity;

import javax.persistence.*;
import java.util.Date;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "google_auth")
public class GoogleAuth {
    private static final String SEQUENCE_NAME = "google_auth_seq";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "google_id", length = 100, unique = true, nullable = false)
    private String googleId;

    @Column(name = "access_token", length = 200, unique = true, nullable = false)
    private String accessToken;

    @Column(name = "refresh_token", length = 200, unique = true, nullable = false)
    private String refreshToken;

    @Column(name = "token_expires_data_in_millis", nullable = false)
    private Long tokenExpiresDateInMillis;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
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
