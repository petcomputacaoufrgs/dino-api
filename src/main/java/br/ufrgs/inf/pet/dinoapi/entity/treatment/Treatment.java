package br.ufrgs.inf.pet.dinoapi.entity.treatment;

import br.ufrgs.inf.pet.dinoapi.constants.TreatmentConstants;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.EssentialContact;
import br.ufrgs.inf.pet.dinoapi.entity.faq.Faq;
import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.entity.user.UserSettings;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "treatment")
public class Treatment extends SynchronizableEntity<Long> {
    @Column(name = "name", length = TreatmentConstants.NAME_MAX, nullable = false)
    private String name;

    @OneToOne(mappedBy = "treatment")
    private Faq faq;

    @OneToMany(mappedBy = "treatment", fetch = FetchType.LAZY)
    private List<UserSettings> userSettings;

    @ManyToMany(mappedBy = "treatments",fetch = FetchType.LAZY)
    private List<EssentialContact> essentialContacts;

    public Treatment() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
