package br.ufrgs.inf.pet.dinoapi.model.contacts;

import br.ufrgs.inf.pet.dinoapi.constants.ContactsConstants;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataLocalIdModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PhoneDataModel extends SynchronizableDataLocalIdModel<Long> {

    @NotNull(message = ContactsConstants.TYPE_NULL_MESSAGE)
    private short type;

    @NotNull(message = ContactsConstants.NUMBER_NULL_MESSAGE)
    @Size(max = ContactsConstants.NUMBER_MAX, message = ContactsConstants.NUMBER_MAX_MESSAGE)
    private String number;

    private Long contactId;

    private Long essentialContactId;

    private Long originalEssentialPhoneId;

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

    public Long getEssentialContactId() {
        return essentialContactId;
    }

    public void setEssentialContactId(Long essentialContactId) {
        this.essentialContactId = essentialContactId;
    }

    public Long getOriginalEssentialPhoneId() {
        return originalEssentialPhoneId;
    }

    public void setOriginalEssentialPhoneId(Long originalEssentialPhoneId) {
        this.originalEssentialPhoneId = originalEssentialPhoneId;
    }
}
