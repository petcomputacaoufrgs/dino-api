package br.ufrgs.inf.pet.dinoapi.configuration.security;

import br.ufrgs.inf.pet.dinoapi.entity.auth.Auth;

public class DinoCredentials {
    private Auth auth;

    public DinoCredentials(Auth auth) {
        this.auth = auth;
    }

    public Auth getAuth() {
        return auth;
    }
}
