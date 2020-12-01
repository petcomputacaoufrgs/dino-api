package br.ufrgs.inf.pet.dinoapi.model.synchronizable;

import br.ufrgs.inf.pet.dinoapi.constants.SynchronizableConstants;
import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Model with complete visible data of synchronizable entity
 *
 * @param <ID> Type of entity ID
 * @param <ENTITY> Synchronizable entity
 */
public abstract class SynchronizableDataModel<ID, ENTITY extends SynchronizableEntity<ID>> implements SynchronizableModel<ID> {
    private ID id;

    @NotNull(message= SynchronizableConstants.LAST_UPDATE_CANNOT_BE_NULL)
    private LocalDateTime lastUpdate;

    public SynchronizableDataModel() { }

    public SynchronizableDataModel(ENTITY entity) {
        this.id = entity.getId();
        this.lastUpdate = entity.getLastUpdate();
    }

    public ID getId() {
        return id;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }
}
