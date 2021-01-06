package br.ufrgs.inf.pet.dinoapi.model.contacts;

import br.ufrgs.inf.pet.dinoapi.constants.ContactsConstants;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataLocalIdModel;

import javax.validation.constraints.NotNull;

public class EssentialContactDataModel extends SynchronizableDataLocalIdModel<Long, Integer> {

    private Long treatmentId;

    @NotNull(message = ContactsConstants.CONTACT_ID_NULL_MESSAGE)
    private Long contactId;

    public Long getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(Long treatmentId) {
        this.treatmentId = treatmentId;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }
}
