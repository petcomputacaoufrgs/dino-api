package br.ufrgs.inf.pet.dinoapi.enumerable;

public enum AuthEnum {

    STAFF(1),
    ADMIN(2),
    USER(3);

    private int value;

    AuthEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
