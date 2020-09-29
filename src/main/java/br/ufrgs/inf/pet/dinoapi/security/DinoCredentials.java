package br.ufrgs.inf.pet.dinoapi.security;

import br.ufrgs.inf.pet.dinoapi.entity.Auth;

public class DinoCredentials {
    private Auth auth;

    public DinoCredentials(Auth auth) {
        this.auth = auth;
    }

    public Auth getAuth() {
        return auth;
    }
}
