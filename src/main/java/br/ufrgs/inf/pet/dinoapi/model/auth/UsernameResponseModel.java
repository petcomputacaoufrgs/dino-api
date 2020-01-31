package br.ufrgs.inf.pet.dinoapi.model.auth;

/**
 * Model para envio do nome do usuário
 *
 * @author joao.silva
 */
public class UsernameResponseModel {
    String name;

    public UsernameResponseModel() {}

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
