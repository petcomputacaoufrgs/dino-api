package br.ufrgs.inf.pet.dinoapi.model.synchronizable;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public abstract class SynchronizableDataLocalIdModel<ID extends Comparable<ID> & Serializable, LOCAL_ID> extends SynchronizableDataModel<ID> {
    public SynchronizableDataLocalIdModel() {}

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private LOCAL_ID localId;

    public LOCAL_ID getLocalId() {
        return localId;
    }

    public void setLocalId(LOCAL_ID localId) {
        this.localId = localId;
    }
}
