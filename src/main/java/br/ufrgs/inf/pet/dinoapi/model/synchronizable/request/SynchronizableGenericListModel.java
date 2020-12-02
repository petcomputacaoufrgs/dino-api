package br.ufrgs.inf.pet.dinoapi.model.synchronizable.request;

import br.ufrgs.inf.pet.dinoapi.constants.SynchronizableConstants;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableModel;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public class SynchronizableGenericListModel<ID extends Comparable<ID>, DATA_TYPE extends SynchronizableModel<ID>> {
    @Valid
    @NotNull(message = SynchronizableConstants.DATA_CANNOT_BE_NULL)
    private List<DATA_TYPE> data;

    public List<DATA_TYPE> getData() {
        return data;
    }

    public void setData(List<DATA_TYPE> data) {
        this.data = data;
    }
}
