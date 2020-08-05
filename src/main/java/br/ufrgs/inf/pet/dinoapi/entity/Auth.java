package br.ufrgs.inf.pet.dinoapi.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "auth")
public class Auth {
    private static final String SEQUENCE_NAME = "auth_seq";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "access_token", length = 260, unique = true, nullable = false)
    private String accessToken;

    @Column(name = "refresh_token", length = 260, unique = true, nullable = false)
    private String refreshToken;

    @Column(name = "token_expires_date", nullable = false)
    private Date tokenExpiresDate;

    @Column(name = "last_update", nullable = false)
    private Date lastUpdate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "auth")
    private List<LogAppError> errors;

    public Auth() {
        this.errors = new ArrayList<>();
    }

    public Long getId() {
        return id;
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

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<LogAppError> getErrors() {
        return errors;
    }

    public void setErrors(List<LogAppError> errors) {
        this.errors = errors;
    }

    public Date getTokenExpiresDate() {
        return tokenExpiresDate;
    }

    public void setTokenExpiresDate(Date tokenExpiresDate) {
        this.tokenExpiresDate = tokenExpiresDate;
    }

    public Boolean tokenIsValid() {
        return (new Date()).getTime() <= this.tokenExpiresDate.getTime();
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
