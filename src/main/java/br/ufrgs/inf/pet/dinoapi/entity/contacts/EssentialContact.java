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
    @Column(name = "name", length = NAME_MAX, nullable = false)
    private String name;

    @Column(name = "description", length = DESCRIPTION_MAX)
    private String description;

    @Column(name = "color")
    private Byte color;

    @OneToMany(mappedBy = "essentialContact", fetch = FetchType.LAZY)
    private List<Contact> contacts;

    @OneToMany(mappedBy = "essentialContact", fetch = FetchType.LAZY)
    private List<EssentialPhone> essentialPhones;

    @ManyToMany
    @JoinTable(name = "essential_contact__treatment",
            joinColumns = {@JoinColumn(name = "essential_contact_id")},
            inverseJoinColumns = {@JoinColumn(name = "treatment_id")})
    private List<Treatment> treatments;

    public EssentialContact() {
    }

    public List<Treatment> getTreatments() {
        return treatments;
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

    public List<Contact> getContacts() {
        return contacts;
    }

    public List<EssentialPhone> getEssentialPhones() {
        return essentialPhones;
    }

    public void setTreatments(List<Treatment> treatments) {
        this.treatments = treatments;
    }
}
