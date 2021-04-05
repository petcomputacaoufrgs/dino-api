package br.ufrgs.inf.pet.dinoapi.security;

import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import javax.security.auth.Subject;
import java.util.Collection;
import java.util.List;

public class DinoAuthenticationToken extends AbstractAuthenticationToken {
    private final DinoCredentials credentials;
    private final DinoUser principal;
    private final Collection<GrantedAuthority> authorities;

    public DinoAuthenticationToken(User user, DinoCredentials credentials, List<GrantedAuthority> authorities) {
        super(authorities);
        final DinoUser principal = new DinoUser(user, authorities);
        this.principal = principal;
        this.credentials = credentials;
        this.authorities = authorities;
        this.setAuthenticated(true);
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.authorities;
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
