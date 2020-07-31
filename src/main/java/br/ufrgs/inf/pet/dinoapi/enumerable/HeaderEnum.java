package br.ufrgs.inf.pet.dinoapi.enumerable;

/**
 * Define a chave dos Headers da API
 *
 * @author joao.silva
 */
public enum HeaderEnum {
    AUTHORIZATION("dino_an"),
    REFRESH("refresh"),
    GOOGLE_REFRESH("google_refresh");

    private String value;

    HeaderEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
