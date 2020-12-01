package br.ufrgs.inf.pet.dinoapi.model.synchronizable;

/**
 * Response model for synchronizable entity
 */
public final class SynchronizableResponseModel {
    private boolean success;
    private String error;
    private SynchronizableDataModel data;

    public void setSuccess(boolean success) {
        this.success = success;
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

    public String getError() {
        return error;
    }

    public boolean isSuccess() {
        return success;
    }
}
