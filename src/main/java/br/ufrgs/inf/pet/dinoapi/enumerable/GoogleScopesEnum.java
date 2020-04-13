package br.ufrgs.inf.pet.dinoapi.enumerable;

/**
 * Define os escopos de permissão do Google
 *
 * @author joao.silva
 */
public enum GoogleScopesEnum {
    CALENDAR("https://www.googleapis.com/auth/calendar"),
    PROFILE("https://www.googleapis.com/auth/userinfo.profile");

    private String scope;

    GoogleScopesEnum(String scope) {
        this.scope = scope;
    }

    public String getScope() {
        return this.scope;
    }
}
