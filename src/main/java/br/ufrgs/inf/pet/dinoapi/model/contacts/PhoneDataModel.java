package br.ufrgs.inf.pet.dinoapi.model.contacts;

import br.ufrgs.inf.pet.dinoapi.constants.ContactsConstants;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataLocalIdModel;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PhoneDataModel extends SynchronizableDataLocalIdModel<Long, Integer> {

    @NotNull(message = ContactsConstants.TYPE_NULL_MESSAGE)
    private short type;

    @NotNull(message = ContactsConstants.NUMBER_NULL_MESSAGE)
    @Size(max = ContactsConstants.NUMBER_MAX, message = ContactsConstants.NUMBER_MAX_MESSAGE)
    private String number;

    @NotNull(message = ContactsConstants.CONTACT_ID_NULL_MESSAGE)
    private Long contactId;

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }
}
