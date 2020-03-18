package br.ufrgs.inf.pet.dinoapi.enumerable;

/**
 * Define os escopos de permissão do Google
 *
 * @author joao.silva
 */
public enum GoogleScopes {
    CALENDAR("https://www.googleapis.com/auth/calendar");

    private String scope;

    GoogleScopes(String scope) {
        this.scope = scope;
    }

    public String getScope() {
        return this.scope;
    }
}
