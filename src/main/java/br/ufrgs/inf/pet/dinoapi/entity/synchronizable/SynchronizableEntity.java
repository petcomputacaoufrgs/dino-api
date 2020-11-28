package br.ufrgs.inf.pet.dinoapi.entity.synchronizable;

import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableModel;
import java.time.LocalDateTime;

public abstract class SynchronizableEntity {
    public SynchronizableEntity() {
        this.setLastUpdate(LocalDateTime.now());
    }

    public abstract LocalDateTime getLastUpdate();

    public abstract void setLastUpdate(LocalDateTime lastUpdate);

    public abstract Long getId();

    public boolean isNewerThan(SynchronizableModel model) {
        final LocalDateTime thisLastUpdate = this.getLastUpdate();
        if (thisLastUpdate != null) {
            final LocalDateTime otherLastUpdate = model.getLastUpdate();
            if (otherLastUpdate != null) {
                return this.getLastUpdate().isAfter(model.getLastUpdate());
            }

            return true;
        }

        return false;
    }

    public boolean isOlderThan(SynchronizableModel model) {
        final LocalDateTime thisLastUpdate = this.getLastUpdate();
        if (thisLastUpdate != null) {
            final LocalDateTime otherLastUpdate = model.getLastUpdate();
            if (otherLastUpdate != null) {
                return this.getLastUpdate().isBefore(model.getLastUpdate());
            }

            return false;
        }

        return true;
    }
}
