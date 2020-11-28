package br.ufrgs.inf.pet.dinoapi.model.synchronizable;

public final class SynchronizableResponseModel {
    private boolean success;
    private String error;
    private SynchronizableDataModel data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public SynchronizableDataModel getData() {
        return data;
    }

    public void setData(SynchronizableDataModel data) {
        this.data = data;
    }
}
