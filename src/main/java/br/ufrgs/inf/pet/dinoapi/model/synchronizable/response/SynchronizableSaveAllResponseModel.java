package br.ufrgs.inf.pet.dinoapi.model.synchronizable.response;

import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataLocalIdModel;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

public class SynchronizableSaveAllResponseModel<
        ID extends Comparable<ID> & Serializable,
        DATA_MODEL extends SynchronizableDataLocalIdModel<ID>>
        implements SynchronizableGenericResponseModel {
    @Valid
    private List<DATA_MODEL> data;

    protected boolean success;

    protected String error;

    protected Integer errorCode;

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

    @Override
    public Integer getErrorCode() {
        return errorCode;
    }

    @Override
    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public List<DATA_MODEL> getData() {
        return data;
    }

    public void setData(List<DATA_MODEL> data) {
        this.data = data;
    }
}
