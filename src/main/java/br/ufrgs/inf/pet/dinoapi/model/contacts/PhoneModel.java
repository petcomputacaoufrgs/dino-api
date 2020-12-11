package br.ufrgs.inf.pet.dinoapi.model.contacts;

import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataModel;

public class PhoneModel extends SynchronizableDataModel<Long> {

    private short type;

    private String number;

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
