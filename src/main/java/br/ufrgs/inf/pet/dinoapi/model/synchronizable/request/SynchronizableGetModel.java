package br.ufrgs.inf.pet.dinoapi.model.synchronizable.request;

import br.ufrgs.inf.pet.dinoapi.constants.SynchronizableConstants;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableIdModel;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Model for get a synchronizable entity
 *
 * @param <ID> Id type of synchronizable
 */
public class SynchronizableGetModel<ID extends Comparable<ID> & Serializable> implements SynchronizableIdModel<ID> {
    @NotNull(message = SynchronizableConstants.ID_CANNOT_BE_NULL)
    protected ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }
}
