package br.ufrgs.inf.pet.dinoapi.enumerable;

public enum GoogleAuthErrorCodeEnum {
    REFRESH_TOKEN(0),
    EXCEPTION(1),
    INVALID_GOOGLE_GRANT_USER(2);

    private int value;

    GoogleAuthErrorCodeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
