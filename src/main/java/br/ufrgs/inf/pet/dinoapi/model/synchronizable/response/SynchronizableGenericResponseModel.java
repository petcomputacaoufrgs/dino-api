package br.ufrgs.inf.pet.dinoapi.model.synchronizable.response;

public interface SynchronizableGenericResponseModel {
    void setSuccess(boolean success);

    void setError(String error);

    String getError();

    boolean isSuccess();
}
