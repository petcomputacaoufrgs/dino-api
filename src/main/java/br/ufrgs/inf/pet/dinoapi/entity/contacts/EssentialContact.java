package br.ufrgs.inf.pet.dinoapi.entity.contacts;

import br.ufrgs.inf.pet.dinoapi.entity.faq.Faq;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "essential_contact")
public class EssentialContact implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String SEQUENCE_NAME = "ess_contact_seq";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faq_id")
    private Faq faq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;

    public EssentialContact() {}

    public EssentialContact(Faq faq, Contact contact) {
        this.faq = faq;
        this.contact = contact;
    }

    public EssentialContact(Contact contact) {
        this.contact = contact;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Faq getFaq() {
        return faq;
    }

    public void setFaq(Faq faq) {
        this.faq = faq;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
