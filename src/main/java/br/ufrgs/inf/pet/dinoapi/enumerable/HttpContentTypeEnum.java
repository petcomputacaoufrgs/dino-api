package br.ufrgs.inf.pet.dinoapi.enumerable;

public enum HttpContentTypeEnum {
    JSON("application/json");

    private String value;

    HttpContentTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}