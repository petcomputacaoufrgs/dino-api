package br.ufrgs.inf.pet.dinoapi.websocket.interceptor;

import br.ufrgs.inf.pet.dinoapi.security.DinoAuthenticationToken;
import br.ufrgs.inf.pet.dinoapi.security.DinoGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class WebSocketUser implements UserDetails {

    private List<DinoGrantedAuthority> authorityList;

    private String username;

    public WebSocketUser(DinoAuthenticationToken dinoAuthenticationToken) {
        this.username = dinoAuthenticationToken.getCredentials().getAuth().getWebSocketToken();
        this.authorityList = dinoAuthenticationToken.getPrincipal().getAuthorities();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
