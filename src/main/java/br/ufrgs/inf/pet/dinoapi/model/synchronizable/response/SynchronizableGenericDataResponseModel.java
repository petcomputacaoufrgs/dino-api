package br.ufrgs.inf.pet.dinoapi.model.synchronizable.response;

/**
 * Generic Response model for synchronizable request with generic data
 *
 * @param <DATA_TYPE> Type of response data
 */
public class SynchronizableGenericDataResponseModel<DATA_TYPE> extends SynchronizableGenericResponseModel {
    private DATA_TYPE data;

    public DATA_TYPE getData() {
        return data;
    }

    public void setData(DATA_TYPE data) {
        this.data = data;
    }
}
