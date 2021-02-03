package br.ufrgs.inf.pet.dinoapi.enumerable;

public enum AuthEnum {

    CLIENT(1),
    STAFF(2),
    ADMIN(3),
    USER(4);

    private int value;

    AuthEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
