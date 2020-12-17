package br.ufrgs.inf.pet.dinoapi.model.synchronizable;

import br.ufrgs.inf.pet.dinoapi.constants.SynchronizableConstants;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Model with complete visible data of synchronizable entity
 *
 * @param <ID> Type of entity ID
 */
public abstract class SynchronizableDataModel<ID extends Comparable<ID> & Serializable> implements SynchronizableModel<ID> {
    private ID id;

    @NotNull(message= SynchronizableConstants.LAST_UPDATE_CANNOT_BE_NULL)
    private LocalDateTime lastUpdate;


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
