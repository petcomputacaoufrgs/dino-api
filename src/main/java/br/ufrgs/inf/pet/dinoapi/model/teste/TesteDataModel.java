package br.ufrgs.inf.pet.dinoapi.model.teste;

import br.ufrgs.inf.pet.dinoapi.entity.teste.TesteEntity;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataModel;

public class TesteDataModel extends SynchronizableDataModel<Long, TesteEntity> {
    private String name;

    public TesteDataModel() {
        super();
    }

    public TesteDataModel(TesteEntity entity) {
        super(entity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
