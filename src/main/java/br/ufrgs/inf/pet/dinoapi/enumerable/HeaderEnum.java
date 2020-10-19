package br.ufrgs.inf.pet.dinoapi.enumerable;

public enum HeaderEnum {
    AUTHORIZATION("dino_an"),
    WS_AUTHORIZATION("ws_dino_an");

    private String value;

    HeaderEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
