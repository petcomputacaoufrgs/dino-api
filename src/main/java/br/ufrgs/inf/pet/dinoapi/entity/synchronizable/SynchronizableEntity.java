package br.ufrgs.inf.pet.dinoapi.entity.synchronizable;

import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableModel;

import java.util.Date;


public abstract class SynchronizableEntity<T extends SynchronizableEntity> {
    protected Long id;

    protected Date lastUpdate;

    public abstract Date getLastUpdate();

    public abstract Long getId();

    public boolean isMoreUpdated(SynchronizableModel<T> model) {
        final Date thisLastUpdate = this.getLastUpdate();
        if (thisLastUpdate != null) {
            final Date otherLastUpdate = model.getLastUpdate();
            if (otherLastUpdate != null) {
                return this.lastUpdate.compareTo(model.getLastUpdate()) > 0;
            }

            return true;
        }

        return false;
    }
}
