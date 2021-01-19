package br.ufrgs.inf.pet.dinoapi.model.synchronizable.request;

import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataLocalIdModel;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

public class SynchronizableSaveSyncModel<ID extends Comparable<ID> & Serializable, DATA_MODEL extends SynchronizableDataLocalIdModel<ID>> {
    @Valid
    private List<DATA_MODEL> save;

    public List<DATA_MODEL> getSave() {
        return save;
    }

    public void setSave(List<DATA_MODEL> save) {
        this.save = save;
    }
}
