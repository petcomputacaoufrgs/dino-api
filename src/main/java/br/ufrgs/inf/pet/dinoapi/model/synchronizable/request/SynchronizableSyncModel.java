package br.ufrgs.inf.pet.dinoapi.model.synchronizable.request;

import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableModel;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

public class SynchronizableSyncModel<ID extends Comparable<ID> & Serializable, LOCAL_ID, DATA_TYPE extends SynchronizableModel<ID, LOCAL_ID>> {
    @Valid
    private List<DATA_TYPE> save;

    @Valid
    private List<SynchronizableDeleteModel<ID, LOCAL_ID>> delete;

    public List<DATA_TYPE> getSave() {
        return save;
    }

    public void setSave(List<DATA_TYPE> save) {
        this.save = save;
    }

    public List<SynchronizableDeleteModel<ID, LOCAL_ID>> getDelete() {
        return delete;
    }

    public void setDelete(List<SynchronizableDeleteModel<ID, LOCAL_ID>> delete) {
        this.delete = delete;
    }
}
