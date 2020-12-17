package br.ufrgs.inf.pet.dinoapi.enumerable;

public enum FontSize {
    DEFAULT(1),
    LARGE(2),
    LARGER(3);

    private int value;

    FontSize(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
