package br.ufrgs.inf.pet.dinoapi.enumerable;

public enum GoogleAPIHeaderEnum {
    AUTHORIZATION("Authorization");

    private String value;

    GoogleAPIHeaderEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
