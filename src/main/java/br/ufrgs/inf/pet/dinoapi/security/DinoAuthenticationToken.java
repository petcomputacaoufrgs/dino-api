package br.ufrgs.inf.pet.dinoapi.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import javax.security.auth.Subject;
import java.util.List;

public class DinoAuthenticationToken extends AbstractAuthenticationToken {
    private DinoCredentials credentials;
    private DinoUser principal;

    public DinoAuthenticationToken(DinoUser principal, DinoCredentials credentials, List<DinoGrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        this.setAuthenticated(true);
    }

    @Override
    public DinoCredentials getCredentials() {
        return this.credentials;
    }

    @Override
    public DinoUser getPrincipal() {
        return this.principal;
    }

    @Override
    public boolean implies(Subject subject) {
        return false;
    }
}
