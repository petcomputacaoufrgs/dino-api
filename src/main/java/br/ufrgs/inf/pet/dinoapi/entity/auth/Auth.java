package br.ufrgs.inf.pet.dinoapi.entity.auth;

import br.ufrgs.inf.pet.dinoapi.constants.AuthConstants;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "auth")
public class Auth {
    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "id", nullable = false)
    protected Long id;

    @Column(name = "access_token", length = AuthConstants.ACCESS_TOKEN_MAX, unique = true)
    private String accessToken;

    @Column(name = "refresh_token", length = AuthConstants.REFRESH_TOKEN_MAX, unique = true)
    private String refreshToken;

    @Column(name = "web_socket_token", length = AuthConstants.WS_TOKEN_MAX, unique = true)
    private String webSocketToken;

    @Column(name = "web_socket_connected", nullable = false)
    private Boolean webSocketConnected;

    @Column(name = "last_token_refresh")
    private LocalDateTime lastTokenRefresh;

    @Column(name = "responsible_token", length = AuthConstants.RESPONSIBLE_TOKEN_MAX)
    private String responsibleToken;

    @Column(name = "responsible_code", length = AuthConstants.RESPONSIBLE_CODE_LENGTH)
    private String responsibleCode;

    @Column(name = "responsible_iv", length = AuthConstants.RESPONSIBLE_CODE_LENGTH)
    private String responsibleIV;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
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

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public LocalDateTime getLastTokenRefresh() {
        return lastTokenRefresh;
    }

    public void setLastTokenRefresh(LocalDateTime lastTokenRefresh) {
        this.lastTokenRefresh = lastTokenRefresh;
    }

    public String getResponsibleToken() {
        return responsibleToken;
    }

    public void setResponsibleToken(String responsibleHash) {
        this.responsibleToken = responsibleHash;
    }

    public String getResponsibleCode() {
        return responsibleCode;
    }

    public void setResponsibleCode(String responsibleCode) {
        this.responsibleCode = responsibleCode;
    }

    public String getResponsibleIV() {
        return responsibleIV;
    }

    public void setResponsibleIV(String responsibleSalt) {
        this.responsibleIV = responsibleSalt;
    }
}
