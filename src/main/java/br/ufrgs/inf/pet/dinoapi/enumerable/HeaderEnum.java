package br.ufrgs.inf.pet.dinoapi.enumerable;

public enum HeaderEnum {
    AUTHORIZATION("dino_an"),
    AUTHORIZATION_EXPIRES_DATE("authorization_expires_date"),
    REFRESH("refresh"),
    GOOGLE_REFRESH("google_refresh"),
    GOOGLE_EXPIRES_DATE("google_expires_date");

    private String value;

    HeaderEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
