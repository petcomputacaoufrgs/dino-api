package br.ufrgs.inf.pet.dinoapi.model.synchronizable;

import java.io.Serializable;

public abstract class SynchronizableDataLocalIdModel<ID extends Comparable<ID> & Serializable, LOCAL_ID> extends SynchronizableDataModel<ID> {
    private LOCAL_ID localId;

    public LOCAL_ID getLocalId() {
        return localId;
    }

    public void setLocalId(LOCAL_ID localId) {
        this.localId = localId;
    }
}
