package br.ufrgs.inf.pet.dinoapi.entity;

import javax.persistence.*;
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

    @Column(name = "refresh_token", length = 200, unique = true, nullable = false)
    private String refreshToken;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public GoogleAuth() {}

    public GoogleAuth(String googleId, String refreshToken, User user) {
        this.googleId = googleId;
        this.refreshToken = refreshToken;
        this.user = user;
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
}
