package br.ufrgs.inf.pet.dinoapi.enumerable;

public enum AuthEnum {
    STAFF("STAFF"),
    ADMIN("ADMIN"),
    USER("USER");

    private final String value;

    AuthEnum(String value)
    {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
