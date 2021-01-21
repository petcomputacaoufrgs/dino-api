package br.ufrgs.inf.pet.dinoapi.enumerable;

public enum LanguageEnum implements IntEnumInterface {

    PORTUGUESE(1),
    ENGLISH(2);

    private int value;

    LanguageEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

}
