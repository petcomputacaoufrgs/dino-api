package br.ufrgs.inf.pet.dinoapi.security;

import org.springframework.security.core.GrantedAuthority;

public class DinoGrantedAuthority implements GrantedAuthority {
    private String authority;

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}