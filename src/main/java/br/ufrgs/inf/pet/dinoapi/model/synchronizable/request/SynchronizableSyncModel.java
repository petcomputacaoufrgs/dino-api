package br.ufrgs.inf.pet.dinoapi.model.synchronizable.request;

import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataLocalIdModel;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

public class SynchronizableSyncModel<ID extends Comparable<ID> & Serializable, LOCAL_ID, DATA_MODEL extends SynchronizableDataLocalIdModel<ID, LOCAL_ID>> {
    @Valid
    private List<DATA_MODEL> save;

    @Valid
    private List<SynchronizableDeleteModel<ID>> delete;

    public List<DATA_MODEL> getSave() {
        return save;
    }

    public void setSave(List<DATA_MODEL> save) {
        this.save = save;
    }

    public List<SynchronizableDeleteModel<ID>> getDelete() {
        return delete;
    }

    public void setDelete(List<SynchronizableDeleteModel<ID>> delete) {
        this.delete = delete;
    }
}
