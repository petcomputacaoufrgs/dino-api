package br.ufrgs.inf.pet.dinoapi.model.test_connection;

/**
 * Model para envio e recebimento dos dados de teste de comunicação
 *
 * @author joao.silva
 */
public class TestModel {
    String texto;

    public TestModel() {}

    public TestModel(String texto){
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
