package br.ufrgs.inf.pet.dinoapi.enumerable;

public enum HttpHeaderEnum {
    CONTENT_TYPE("Content-Type");

    private String value;

    HttpHeaderEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
