package br.ufrgs.inf.pet.dinoapi.model.synchronizable;

import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;

import java.util.List;

/**
 * Response model for list of synchronizable entity
 */
public final class SynchronizableListResponseModel<ENTITY extends SynchronizableEntity<ID>, ID, DATA_MODEL extends SynchronizableDataModel<ENTITY, ID>> {
    private boolean success;
    private String error;
    private List<DATA_MODEL> data;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<DATA_MODEL> getData() {
        return data;
    }

    public void setData(List<DATA_MODEL> data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public boolean isSuccess() {
        return success;
    }
}
