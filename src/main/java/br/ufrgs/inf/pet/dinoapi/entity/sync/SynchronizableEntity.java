package br.ufrgs.inf.pet.dinoapi.entity.sync;

import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableModel;

import javax.persistence.Column;
import java.util.Date;


public abstract class SynchronizableEntity<T extends SynchronizableEntity> {
    protected Long id;

    @Column(name = "last_update", nullable = false)
    private Date lastUpdate;

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    protected abstract Long getId();

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
