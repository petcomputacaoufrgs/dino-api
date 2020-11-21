package br.ufrgs.inf.pet.dinoapi.enumerable;

public enum GoogleScopeEnum {
    SCOPE_CONTACT("https://www.googleapis.com/auth/contacts");

    private String scope;

    GoogleScopeEnum(String scope) {
        this.scope = scope;
    }

    public static GoogleScopeEnum findByScope(String scope){
        for(GoogleScopeEnum gs : values()){
            if( gs.scope.equals(scope)){
                return gs;
            }
        }
        return null;
    }
}

