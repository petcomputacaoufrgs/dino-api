package br.ufrgs.inf.pet.dinoapi.model.synchronizable.response;

import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableModel;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.request.SynchronizableSyncModel;

import java.io.Serializable;

public class SynchronizableSyncResponseModel<ID extends Comparable<ID> & Serializable, LOCAL_ID, DATA_TYPE extends SynchronizableModel<ID, LOCAL_ID>>
        extends SynchronizableSyncModel<ID, LOCAL_ID, DATA_TYPE> implements SynchronizableGenericResponseModel {

    protected boolean success;
    protected String error;

    @Override
    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String getError() {
        return error;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }
}
