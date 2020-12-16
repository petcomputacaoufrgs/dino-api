package br.ufrgs.inf.pet.dinoapi.model.synchronizable.response;

import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataLocalIdModel;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

public class SynchronizableSyncResponseModel<
        ID extends Comparable<ID> & Serializable,
        LOCAL_ID,
        DATA_MODEL extends SynchronizableDataLocalIdModel<ID, LOCAL_ID>>
        implements SynchronizableGenericResponseModel {

    @Valid
    private List<DATA_MODEL> save;

    @Valid
    private List<ID> delete;

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

    public List<DATA_MODEL> getSave() {
        return save;
    }

    public void setSave(List<DATA_MODEL> save) {
        this.save = save;
    }

    public List<ID> getDelete() {
        return delete;
    }

    public void setDelete(List<ID> delete) {
        this.delete = delete;
    }
}
