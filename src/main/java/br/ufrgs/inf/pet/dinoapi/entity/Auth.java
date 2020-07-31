package br.ufrgs.inf.pet.dinoapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.util.Date;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "auth")
public class Auth {
    private static final long serialVersionUID = 1L;

    private static final String SEQUENCE_NAME = "auth_seq";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Column(name = "access_token", length = 260, unique = true)
    private String accessToken;

    @Column(name = "token_expires_data_in_millis")
    private Long tokenExpiresDateInMillis;

    public Long getId() {
        return id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Auth() {}

    public Auth(String accessToken, Long tokenExpiresDateInMillis) {
        this.accessToken = accessToken;
        this.tokenExpiresDateInMillis = tokenExpiresDateInMillis;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getTokenExpiresDateInMillis() {
        return tokenExpiresDateInMillis;
    }

    public Boolean tokenIsValid() {
        return (new Date()).getTime() <= this.tokenExpiresDateInMillis;
    }
}
