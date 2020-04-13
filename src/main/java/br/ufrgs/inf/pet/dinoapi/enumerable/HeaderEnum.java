package br.ufrgs.inf.pet.dinoapi.enumerable;

/**
 * Define a chave dos Headers da API
 *
 * @author joao.silva
 */
public enum HeaderEnum {
    AUTHORIZATION("Authorization"),
    REFRESH("Refresh"),
    GOOGLE_REFRESH("Google Refresh");

    private String value;

    HeaderEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
