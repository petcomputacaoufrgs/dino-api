package br.ufrgs.inf.pet.dinoapi.entity.auth.google;
import br.ufrgs.inf.pet.dinoapi.constants.GoogleAuthConstants;
import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "google_access_token")
public class GoogleAccessToken {
    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "id", nullable = false)
    protected Long id;

    @Column(name = "access_token", length = GoogleAuthConstants.ACCESS_TOKEN_MAX, unique = true, nullable = false)
    private String accessToken;

    @Column(name = "expiration", length = GoogleAuthConstants.ACCESS_TOKEN_MAX, unique = true, nullable = false)
    private LocalDateTime expiration;

    @OneToOne
    @JoinColumn(name = "google_auth_id", nullable = false)
    private GoogleAuth googleAuth;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public GoogleAuth getGoogleAuth() {
        return googleAuth;
    }

    public void setGoogleAuth(GoogleAuth googleAuth) {
        this.googleAuth = googleAuth;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    public void setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
    }
}
