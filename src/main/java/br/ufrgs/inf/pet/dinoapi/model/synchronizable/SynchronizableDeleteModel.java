package br.ufrgs.inf.pet.dinoapi.model.synchronizable;

import java.time.LocalDateTime;

public final class SynchronizableDeleteModel implements SynchronizableModel {
    protected Long id;

    protected LocalDateTime lastUpdate;

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
