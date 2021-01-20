package br.ufrgs.inf.pet.dinoapi.enumerable;

public enum FontSizeEnum implements IntEnumInterface {
    DEFAULT(1),
    LARGE(2),
    LARGER(3);

    private int value;

    FontSizeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
