package br.ufrgs.inf.pet.dinoapi.enumerable;

public enum ColorTheme {
    CLASSIC(1),
    DARK(2),
    DALTONIAN(3);

    private int value;

    ColorTheme(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}