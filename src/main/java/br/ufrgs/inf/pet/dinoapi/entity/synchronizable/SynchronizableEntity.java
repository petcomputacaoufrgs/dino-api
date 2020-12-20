package br.ufrgs.inf.pet.dinoapi.entity.synchronizable;

import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableModel;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.AUTO;

/**
 * Base for Synchronizable Entity
 * @param <ID> Type of synchronizable entity id
 */
@MappedSuperclass
public abstract class SynchronizableEntity<ID extends Comparable<ID> & Serializable> implements Serializable {
    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "id", nullable = false)
    protected ID id;

    @Column(name = "last_update", nullable = false)
    protected LocalDateTime lastUpdate;

    public SynchronizableEntity() {
        this.setLastUpdate(LocalDateTime.now());
    }

    public LocalDateTime getLastUpdate() {
        return this.lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public ID getId() {
        return this.id;
    }

    public void setId(ID id) {
        this.id = id;
    }

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
