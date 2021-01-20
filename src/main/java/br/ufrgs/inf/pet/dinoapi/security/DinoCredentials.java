package br.ufrgs.inf.pet.dinoapi.security;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;

public class DinoCredentials {
    private final Auth auth;

    public DinoCredentials(Auth auth) {
        this.auth = auth;
    }

    public Auth getAuth() {
        return auth;
    }
}
