package br.ufrgs.inf.pet.dinoapi.model.synchronizable.request;

import br.ufrgs.inf.pet.dinoapi.constants.SynchronizableConstants;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableModel;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Model for delete an synchronizable entity
 * @param <ID> Id type of synchronizable entity
 */
public class SynchronizableDeleteModel<ID extends Comparable<ID> & Serializable, LOCAL_ID>
        extends SynchronizableGetModel<ID> implements SynchronizableModel<ID, LOCAL_ID> {
    @NotNull(message= SynchronizableConstants.LAST_UPDATE_CANNOT_BE_NULL)
    protected LocalDateTime lastUpdate;

    protected LOCAL_ID localId;

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public LOCAL_ID getLocalId() {
        return localId;
    }

    public void setLocalId(LOCAL_ID localId) {
        this.localId = localId;
    }
}
