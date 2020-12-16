package br.ufrgs.inf.pet.dinoapi.model.synchronizable.response;

/**
 * Generic response model for synchronizable request with generic data
 *
 * @param <DATA_TYPE> Type of response data
 */
public class SynchronizableGenericDataResponseModelImpl<DATA_TYPE> extends SynchronizableGenericResponseModelImpl {
    private DATA_TYPE data;

    public DATA_TYPE getData() {
        return data;
    }

    public void setData(DATA_TYPE data) {
        this.data = data;
    }
}
