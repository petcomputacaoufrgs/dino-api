package br.ufrgs.inf.pet.dinoapi.model.synchronizable.request;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

public class SynchronizableSyncDeleteModel<ID extends Comparable<ID> & Serializable> {
    @Valid
    private List<SynchronizableDeleteModel<ID>> delete;

    public List<SynchronizableDeleteModel<ID>> getDelete() {
        return delete;
    }

    public void setDelete(List<SynchronizableDeleteModel<ID>> delete) {
        this.delete = delete;
    }
}
