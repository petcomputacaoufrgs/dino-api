package br.ufrgs.inf.pet.dinoapi.entity.contacts;

import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;

import javax.persistence.*;

import static br.ufrgs.inf.pet.dinoapi.constants.ContactsConstants.NUMBER_MAX;

@Entity
@Table(name = "phone")
public class Phone extends SynchronizableEntity<Long> {

    @Column(name = "type", nullable = false)
    private short type;

    @Column(name = "number", length = NUMBER_MAX, nullable = false)
    private String number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "essential_contact_id")
    private EssentialContact essentialContact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id")
    private Contact contact;

    public Phone(){}

    public Long getId() {
        return id;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public EssentialContact getEssentialContact() {
        return essentialContact;
    }

    public void setEssentialContact(EssentialContact essentialContact) {
        this.essentialContact = essentialContact;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

}
