package br.ufrgs.inf.pet.dinoapi.enumerable;

public enum ColorTheme {
    LIGHT(1),
    DARK(2),
    DALTONIAN(3),
    DEVICE(4);

    private int value;

    ColorTheme(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}