package br.ufrgs.inf.pet.dinoapi.model.synchronizable;

import br.ufrgs.inf.pet.dinoapi.constants.SynchronizableConstants;

import javax.validation.constraints.NotNull;

/**
 * Model for get a synchronizable entity
 * @param <ID> Id type of synchronizable
 */
public final class SynchronizableGetModel<ID> implements SynchronizableIdModel<ID> {
    @NotNull(message= SynchronizableConstants.ID_CANNOT_BE_NULL)
    private ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }
}
