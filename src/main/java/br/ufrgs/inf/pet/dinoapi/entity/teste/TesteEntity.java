package br.ufrgs.inf.pet.dinoapi.entity.teste;

import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "teste")
public class TesteEntity extends SynchronizableEntity<TesteEntity> {
    @Column(name = "name", nullable = false)
    protected String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
