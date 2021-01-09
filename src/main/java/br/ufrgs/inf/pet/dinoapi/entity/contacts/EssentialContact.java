package br.ufrgs.inf.pet.dinoapi.entity.contacts;

import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.entity.treatment.Treatment;

import javax.persistence.*;
import java.util.List;

import static br.ufrgs.inf.pet.dinoapi.constants.ContactsConstants.DESCRIPTION_MAX;
import static br.ufrgs.inf.pet.dinoapi.constants.ContactsConstants.NAME_MAX;

@Entity
@Table(name = "essential_contact")
public class EssentialContact extends SynchronizableEntity<Long> {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "treatment_ids")
    private List<Treatment> treatments;

    @Column(name = "name", length = NAME_MAX, nullable = false)
    private String name;

    @Column(name = "description", length = DESCRIPTION_MAX)
    private String description;

    @Column(name = "color")
    private Byte color;

    @OneToMany(mappedBy = "essentialContact", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Phone> phones;

    public EssentialContact() {}

    public EssentialContact(List<Treatment> treatments, String name, String description, Byte color, List<Phone> phones) {
        this.treatments = treatments;
        this.name = name;
        this.description = description;
        this.color = color;
        this.phones = phones;
    }

    public List<Treatment> getTreatments() {
        return treatments;
    }

    public void setTreatments(List<Treatment> treatments) {
        this.treatments = treatments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Byte getColor() {
        return color;
    }

    public void setColor(Byte color) {
        this.color = color;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }
}
