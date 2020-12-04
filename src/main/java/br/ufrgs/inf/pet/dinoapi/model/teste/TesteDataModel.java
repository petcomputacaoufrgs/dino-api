package br.ufrgs.inf.pet.dinoapi.model.teste;

import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataModel;

public class TesteDataModel extends SynchronizableDataModel<Long> {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
