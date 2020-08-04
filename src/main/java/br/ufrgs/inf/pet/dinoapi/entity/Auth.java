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

    @Column(name = "token_expires_data_in_millis", nullable = false)
    private Long tokenExpiresDateInMillis;

    @Column(name = "user_agent", nullable = false)
    private String userAgent;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "auth")
    private List<LogAppError> errors;

    public Auth() {
        this.errors = new ArrayList<>();
    }

    public Auth(String accessToken, Long tokenExpiresDateInMillis, String userAgent) {
        this.accessToken = accessToken;
        this.tokenExpiresDateInMillis = tokenExpiresDateInMillis;
        this.userAgent = userAgent;
        this.errors = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public List<LogAppError> getErrors() {
        return errors;
    }

    public void setErrors(List<LogAppError> errors) {
        this.errors = errors;
    }

    public Long getTokenExpiresDateInMillis() {
        return tokenExpiresDateInMillis;
    }

    public Boolean tokenIsValid() {
        return (new Date()).getTime() <= this.tokenExpiresDateInMillis;
    }

}
