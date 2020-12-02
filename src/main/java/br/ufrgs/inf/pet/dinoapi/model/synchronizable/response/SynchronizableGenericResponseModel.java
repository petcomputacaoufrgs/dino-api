package br.ufrgs.inf.pet.dinoapi.model.synchronizable.response;

/**
 * Generic Response model for synchronizable request
 *
 * @param <DATA_TYPE> Type of response data
 */
public abstract class SynchronizableGenericResponseModel<DATA_TYPE> {
    private boolean success;
    private String error;
    private DATA_TYPE data;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setError(String error) {
        this.error = error;
    }

    public DATA_TYPE getData() {
        return data;
    }

    public void setData(DATA_TYPE data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public boolean isSuccess() {
        return success;
    }
}
