package br.ufrgs.inf.pet.dinoapi.enumerable;

public enum KidsSpaceColorEnum {
    DEFAULT("default"),
    PINK("pink"),
    BLUE("blue"),
    RED("red");

    private String value;

    KidsSpaceColorEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
