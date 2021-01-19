package br.ufrgs.inf.pet.dinoapi.entity.contacts;

import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;

import javax.persistence.*;
import java.util.List;

import static br.ufrgs.inf.pet.dinoapi.constants.ContactsConstants.NUMBER_MAX;

@Entity
@Table(name = "phone")
public class Phone extends SynchronizableEntity<Long> {
    @Column(name = "type", nullable = false)
    private short type;

    @Column(name = "number", length = NUMBER_MAX, nullable = false)
    private String number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_essential_phone")
    private Phone originalEssentialPhone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "essential_contact_id")
    private EssentialContact essentialContact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id")
    private Contact contact;

    @OneToMany(mappedBy = "originalEssentialPhone", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Phone> derivativePhones;

    public Phone() {
    }

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

    public Phone getOriginalEssentialPhone() {
        return originalEssentialPhone;
    }

    public void setOriginalEssentialPhone(Phone originalEssentialPhone) {
        this.originalEssentialPhone = originalEssentialPhone;
    }

    public List<Phone> getDerivativePhones() {
        return derivativePhones;
    }

    public void setDerivativePhones(List<Phone> derivativePhones) {
        this.derivativePhones = derivativePhones;
    }
}
