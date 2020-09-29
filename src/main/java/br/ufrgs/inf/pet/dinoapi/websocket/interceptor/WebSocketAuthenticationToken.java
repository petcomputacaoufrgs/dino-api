package br.ufrgs.inf.pet.dinoapi.websocket.interceptor;

import br.ufrgs.inf.pet.dinoapi.context.SpringContext;
import br.ufrgs.inf.pet.dinoapi.security.DinoAuthenticationToken;
import br.ufrgs.inf.pet.dinoapi.security.DinoCredentials;
import br.ufrgs.inf.pet.dinoapi.service.auth.AuthServiceImpl;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class WebSocketAuthenticationToken extends AbstractAuthenticationToken {
    private DinoCredentials credentials;
    private WebSocketUser principal;

    public WebSocketAuthenticationToken(DinoAuthenticationToken dinoAuthenticationToken) {
        super(dinoAuthenticationToken.getAuthorities());
        this.credentials = dinoAuthenticationToken.getCredentials();
        this.principal = new WebSocketUser(dinoAuthenticationToken);
        this.setAuthenticated(true);
        this.setWebSocketConnected();
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    private void setWebSocketConnected() {
        AuthServiceImpl authService = this.getAuthService();
        authService.setWebSocketConnected();
    }

    private AuthServiceImpl getAuthService() {
        return SpringContext.getBean(AuthServiceImpl.class);
    }
}
