package br.ufrgs.inf.pet.dinoapi.model.teste;

import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataModel;

public class TesteDataModel extends SynchronizableDataModel {
    private String name;

    public TesteDataModel(SynchronizableEntity entity) {
        super(entity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
