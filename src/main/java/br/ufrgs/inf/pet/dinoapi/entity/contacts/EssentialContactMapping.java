package br.ufrgs.inf.pet.dinoapi.entity.contacts;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "essential_contact_mapping")
public class EssentialContactMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String SEQUENCE_NAME = "ess_contact_map_seq";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "essential_contact_id")
    private EssentialContact essentialContact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;

    public EssentialContactMapping() {
    }

    public EssentialContactMapping(EssentialContact essentialContact, Contact contact) {
        this.essentialContact = essentialContact;
        this.contact = contact;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EssentialContact getEssentialContact() {
        return essentialContact;
    }

    public void setEssentialContact(EssentialContact essentialContact) {
        this.essentialContact = essentialContact;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
