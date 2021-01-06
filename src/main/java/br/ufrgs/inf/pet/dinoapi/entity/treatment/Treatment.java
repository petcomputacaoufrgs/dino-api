package br.ufrgs.inf.pet.dinoapi.entity.treatment;

import br.ufrgs.inf.pet.dinoapi.constants.TreatmentConstants;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.EssentialContact;
import br.ufrgs.inf.pet.dinoapi.entity.faq.Faq;
import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.entity.user.UserSettings;

import javax.persistence.*;
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

    @OneToMany(mappedBy = "treatment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EssentialContact> essentialContacts;

    public Treatment() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserSettings> getUserSettings() {
        return userSettings;
    }

    public void setUserSettings(List<UserSettings> userSettings) {
        this.userSettings = userSettings;
    }
}
