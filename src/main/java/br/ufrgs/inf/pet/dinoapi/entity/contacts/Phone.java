package br.ufrgs.inf.pet.dinoapi.entity.contacts;

import br.ufrgs.inf.pet.dinoapi.model.contacts.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "phone")
public class Phone implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String SEQUENCE_NAME = "phone_seq";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "O tipo de número do contato não pode ser nulo.")
    @Column(name = "type")
    private byte type;

    @Basic(optional = false)
    @NotNull(message = "O número do contato não pode ser nulo.")
    @Size(min = 1, max = 30)
    @Column(name = "number", length = 30)
    private String number;

    @JsonIgnore
    @Valid
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "O contato associado não pode ser nulo.")
    @JoinColumn(name = "contact_id")
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

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

}
