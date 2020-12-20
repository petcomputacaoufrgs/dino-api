package br.ufrgs.inf.pet.dinoapi.model.treatment;

import br.ufrgs.inf.pet.dinoapi.constants.TreatmentConstants;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataLocalIdModel;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TreatmentDataModel extends SynchronizableDataLocalIdModel<Long, Integer> {
    @NotNull(message = TreatmentConstants.NAME_NULL_MESSAGE)
    @Size(min = TreatmentConstants.NAME_MIN, max = TreatmentConstants.NAME_MAX, message = TreatmentConstants.NAME_SIZE_MESSAGE)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
