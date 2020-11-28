package br.ufrgs.inf.pet.dinoapi.model.synchronizable;

import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;

import java.time.LocalDateTime;

public abstract class SynchronizableDataModel implements SynchronizableModel {
    private Long id;

    private LocalDateTime lastUpdate;

    public SynchronizableDataModel(SynchronizableEntity entity) {
        this.id = entity.getId();
        this.lastUpdate = entity.getLastUpdate();
    }

    public final Long getId() {
        return id;
    }

    public final LocalDateTime getLastUpdate() {
        return lastUpdate;
    }
}
