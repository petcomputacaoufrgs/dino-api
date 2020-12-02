package br.ufrgs.inf.pet.dinoapi.websocket.model.synchronizable;

public class SynchronizableUpdateModel<DATA_MODEL> {
    private DATA_MODEL data;

    public DATA_MODEL getData() {
        return data;
    }

    public void setData(DATA_MODEL data) {
        this.data = data;
    }
}
