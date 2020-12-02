package br.ufrgs.inf.pet.dinoapi.entity.synchronizable;

import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableModel;
import java.time.LocalDateTime;

/**
 * Base for Synchronizable Entity
 * @param <ID> Type of synchronizable entity id
 */
public abstract class SynchronizableEntity<ID extends Comparable<ID>> {
    public SynchronizableEntity() {
        this.setLastUpdate(LocalDateTime.now());
    }

    public abstract LocalDateTime getLastUpdate();

    public abstract void setLastUpdate(LocalDateTime lastUpdate);

    public abstract ID getId();

    public boolean isOlderOrEqualThan(SynchronizableModel<ID> model) {
        final LocalDateTime thisLastUpdate = this.getLastUpdate();
        if (thisLastUpdate != null) {
            final LocalDateTime otherLastUpdate = model.getLastUpdate();
            if (otherLastUpdate != null) {
                return this.getLastUpdate().isBefore(model.getLastUpdate())
                        || this.getLastUpdate().isEqual(model.getLastUpdate());
            }

            return false;
        }

        return true;
    }

    public boolean isOlderThan(SynchronizableModel<ID> model) {
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
