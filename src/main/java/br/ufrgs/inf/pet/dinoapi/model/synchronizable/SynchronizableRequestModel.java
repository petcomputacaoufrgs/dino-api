package br.ufrgs.inf.pet.dinoapi.model.synchronizable;

public final class SynchronizableRequestModel<DATA_MODEL extends SynchronizableDataModel> {
    private DATA_MODEL data;

    public DATA_MODEL getData() {
        return data;
    }

    public void setData(DATA_MODEL data) {
        this.data = data;
    }
}
