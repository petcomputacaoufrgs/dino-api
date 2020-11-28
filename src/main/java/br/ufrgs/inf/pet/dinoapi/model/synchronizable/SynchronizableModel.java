package br.ufrgs.inf.pet.dinoapi.model.synchronizable;

import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import java.time.LocalDateTime;

public abstract class SynchronizableModel<T extends SynchronizableEntity> {
    protected Long id;

    protected LocalDateTime lastUpdate;

    public abstract T updateEntity(T entity);

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
