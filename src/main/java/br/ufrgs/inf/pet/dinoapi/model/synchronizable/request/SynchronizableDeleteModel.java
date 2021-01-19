package br.ufrgs.inf.pet.dinoapi.model.synchronizable.request;

import br.ufrgs.inf.pet.dinoapi.constants.SynchronizableConstants;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableModel;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Model for delete an synchronizable entity
 *
 * @param <ID> Id type of synchronizable entity
 */
public class SynchronizableDeleteModel<ID extends Comparable<ID> & Serializable>
        extends SynchronizableGetModel<ID> implements SynchronizableModel<ID> {
    @NotNull(message = SynchronizableConstants.LAST_UPDATE_CANNOT_BE_NULL)
    protected ZonedDateTime lastUpdate;

    public ZonedDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
