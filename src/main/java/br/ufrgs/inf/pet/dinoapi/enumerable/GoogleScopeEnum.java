package br.ufrgs.inf.pet.dinoapi.enumerable;

public enum GoogleScopeEnum {
    CONTACTS("https://www.googleapis.com/auth/contacts");

    private String value;

    GoogleScopeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
