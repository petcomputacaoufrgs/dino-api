package br.ufrgs.inf.pet.dinoapi.entity.synchronizable;

import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

import static javax.persistence.GenerationType.AUTO;

@Entity
public abstract class SynchronizableEntity<T extends SynchronizableEntity> {
    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "id", nullable = false)
    protected Long id;

    @Column(name = "last_update", nullable = false)
    protected Date lastUpdate;

    public SynchronizableEntity() {
        this.lastUpdate = new Date();
    }

    public Date getLastUpdate() {
        return this.lastUpdate;
    }

    public Long getId() {
        return this.id;
    }

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
