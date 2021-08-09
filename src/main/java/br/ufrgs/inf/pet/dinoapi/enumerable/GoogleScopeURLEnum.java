package br.ufrgs.inf.pet.dinoapi.enumerable;

public enum GoogleScopeURLEnum {
    SCOPE_CONTACT("https://www.googleapis.com/auth/contacts"),
    SCOPE_CALENDAR("https://www.googleapis.com/auth/calendar");

    private String value;

    GoogleScopeURLEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
