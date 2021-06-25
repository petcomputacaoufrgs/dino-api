package br.ufrgs.inf.pet.dinoapi.enumerable;

public enum PermissionEnum {
    STAFF("STAFF"),
    ADMIN("ADMIN"),
    USER("USER");

    private final String value;

    PermissionEnum(String value)
    {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
