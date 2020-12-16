package br.ufrgs.inf.pet.dinoapi.model.synchronizable.response;

/**
 * Generic response model for synchronizable request
 */
public class SynchronizableGenericResponseModelImpl implements SynchronizableGenericResponseModel {
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
