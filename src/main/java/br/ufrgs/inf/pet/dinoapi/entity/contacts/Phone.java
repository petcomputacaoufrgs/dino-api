package br.ufrgs.inf.pet.dinoapi.entity.contacts;

import br.ufrgs.inf.pet.dinoapi.model.contacts.PhoneModel;
import br.ufrgs.inf.pet.dinoapi.model.contacts.PhoneSaveModel;

import javax.persistence.*;
import java.io.Serializable;

import static br.ufrgs.inf.pet.dinoapi.constants.ContactsConstants.NUMBER_MAX;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "phone")
public class Phone implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String SEQUENCE_NAME = "phone_seq";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "type", nullable = false)
    private short type;

    @Column(name = "number", length = NUMBER_MAX, nullable = false)
    private String number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;

    public Phone(){}

    public Phone(PhoneSaveModel phoneSaveModel, Contact contact){
        this.setType(phoneSaveModel.getType());
        this.setNumber(phoneSaveModel.getNumber());
        this.setContact(contact);
    }

    public Phone(PhoneModel phoneModel, Contact contact){
        this.setType(phoneModel.getType());
        this.setNumber(phoneModel.getNumber());
        this.setContact(contact);
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
