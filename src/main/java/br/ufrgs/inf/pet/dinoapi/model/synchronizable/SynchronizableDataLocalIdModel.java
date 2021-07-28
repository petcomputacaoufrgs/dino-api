package br.ufrgs.inf.pet.dinoapi.model.synchronizable;

import java.io.Serializable;

public abstract class SynchronizableDataLocalIdModel<ID extends Comparable<ID> & Serializable>
        extends SynchronizableDataModel<ID> {
    public SynchronizableDataLocalIdModel() {
    }

    private Integer localId;

    public Integer getLocalId() {
        return localId;
    }

    public void setLocalId(Integer localId) {
        this.localId = localId;
    }
}
