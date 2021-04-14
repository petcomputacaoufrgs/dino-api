package br.ufrgs.inf.pet.dinoapi.model.contacts;

import br.ufrgs.inf.pet.dinoapi.constants.ContactsConstants;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataLocalIdModel;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class EssentialContactDataModel extends SynchronizableDataLocalIdModel<Long> {
    @NotNull(message = ContactsConstants.NAME_NULL_MESSAGE)
    @Size(max = ContactsConstants.NAME_MAX, message = ContactsConstants.NAME_MAX_MESSAGE)
    private String name;

    @Size(max = ContactsConstants.DESCRIPTION_MAX, message = ContactsConstants.DESCRIPTION_MAX_MESSAGE)
    private String description;

    private Byte color;

    private List<Long> treatmentIds;

    public List<Long> getTreatmentIds() {
        return treatmentIds;
    }

    public void setTreatmentIds(List<Long> treatmentIds) {
        this.treatmentIds = treatmentIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Byte getColor() {
        return color;
    }

    public void setColor(Byte color) {
        this.color = color;
    }
}
