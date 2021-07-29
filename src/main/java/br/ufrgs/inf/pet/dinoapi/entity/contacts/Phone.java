package br.ufrgs.inf.pet.dinoapi.entity.contacts;

import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import javax.persistence.*;
import static br.ufrgs.inf.pet.dinoapi.constants.ContactsConstants.NUMBER_MAX;

@Entity
@Table(name = "phone")
public class Phone extends SynchronizableEntity<Long> {
    @Column(name = "type", nullable = false)
    private short type;

    @Column(name = "number", length = NUMBER_MAX, nullable = false)
    private String number;

    @ManyToOne
    @JoinColumn(name = "contact_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Contact contact;

    public Phone() { }

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
