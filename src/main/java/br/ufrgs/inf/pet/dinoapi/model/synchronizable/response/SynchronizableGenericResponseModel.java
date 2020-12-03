package br.ufrgs.inf.pet.dinoapi.model.synchronizable.response;

/**
 * Generic response model for synchronizable request
 */
public class SynchronizableGenericResponseModel {
    protected boolean success;
    protected String error;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public boolean isSuccess() {
        return success;
    }
}
