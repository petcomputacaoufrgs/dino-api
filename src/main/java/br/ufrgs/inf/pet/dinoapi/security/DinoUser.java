package br.ufrgs.inf.pet.dinoapi.security;

import br.ufrgs.inf.pet.dinoapi.entity.user.User;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;
import java.util.List;

public class DinoUser implements UserDetails {

    private List<DinoGrantedAuthority> authorityList;

    private User user;

    public DinoUser(User user) {
        this.user = user;
        this.authorityList = new ArrayList<>();
    }

    public User getUser() {
        return user;
    }

    @Override
    public List<DinoGrantedAuthority> getAuthorities() {
        return this.authorityList;
    }

    public void setAuthorityList(List<DinoGrantedAuthority> authorityList) {
        this.authorityList = authorityList;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
