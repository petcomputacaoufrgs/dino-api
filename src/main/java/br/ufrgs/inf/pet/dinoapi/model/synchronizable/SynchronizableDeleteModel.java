package br.ufrgs.inf.pet.dinoapi.model.synchronizable;

import br.ufrgs.inf.pet.dinoapi.constants.SynchronizableConstants;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Model for delete an synchronizable entity
 * @param <ID> Id type of synchronizable entity
 */
public final class SynchronizableDeleteModel<ID> implements SynchronizableModel<ID> {
    @NotNull(message= SynchronizableConstants.ID_CANNOT_BE_NULL)
    protected ID id;

    @NotNull(message= SynchronizableConstants.LAST_UPDATE_CANNOT_BE_NULL)
    protected LocalDateTime lastUpdate;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
