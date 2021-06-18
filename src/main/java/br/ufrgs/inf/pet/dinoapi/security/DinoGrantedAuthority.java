package br.ufrgs.inf.pet.dinoapi.security;

import org.springframework.security.core.GrantedAuthority;

public class DinoGrantedAuthority implements GrantedAuthority {
    private final String authority;

    public DinoGrantedAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
