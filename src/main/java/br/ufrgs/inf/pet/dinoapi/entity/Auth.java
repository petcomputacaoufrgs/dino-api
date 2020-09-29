package br.ufrgs.inf.pet.dinoapi.entity;

import javax.persistence.*;

import static br.ufrgs.inf.pet.dinoapi.constants.AuthConstants.ACCESS_TOKEN_MAX;
import static br.ufrgs.inf.pet.dinoapi.constants.AuthConstants.WS_TOKEN_MAX;
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

    @Column(name = "access_token", length = ACCESS_TOKEN_MAX, unique = true, nullable = false)
    private String accessToken;

    @Column(name = "web_socket_token", length = WS_TOKEN_MAX, unique = true)
    private String webSocketToken;

    @Column(name = "web_socket_connected", nullable = false)
    private Boolean webSocketConnected;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getWebSocketToken() {
        return webSocketToken;
    }

    public void setWebSocketToken(String webSocketToken) {
        this.webSocketToken = webSocketToken;
    }

    public Boolean getWebSocketConnected() {
        return webSocketConnected;
    }

    public void setWebSocketConnected(Boolean webSocketConnected) {
        this.webSocketConnected = webSocketConnected;
    }
}
