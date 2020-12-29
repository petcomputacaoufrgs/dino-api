package br.ufrgs.inf.pet.dinoapi.model.synchronizable;

import br.ufrgs.inf.pet.dinoapi.constants.SynchronizableConstants;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Model with complete visible data of synchronizable entity
 *
 * @param <ID> Type of entity ID
 */
public abstract class SynchronizableDataModel<ID extends Comparable<ID> & Serializable> implements SynchronizableModel<ID> {
    private ID id;

    @NotNull(message= SynchronizableConstants.LAST_UPDATE_CANNOT_BE_NULL)
    private ZonedDateTime lastUpdate;


    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public ZonedDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
