package br.ufrgs.inf.pet.dinoapi.enumerable;

public enum AuthEnum {

    ADMIN(1),
    USER(2);

    private int value;

    AuthEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
