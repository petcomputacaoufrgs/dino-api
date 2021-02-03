package br.ufrgs.inf.pet.dinoapi.entity.treatment;

import br.ufrgs.inf.pet.dinoapi.constants.TreatmentConstants;
import br.ufrgs.inf.pet.dinoapi.entity.contacts.EssentialContact;
import br.ufrgs.inf.pet.dinoapi.entity.faq.FaqItem;
import br.ufrgs.inf.pet.dinoapi.entity.faq.TreatmentQuestion;
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

    @OneToMany(mappedBy = "treatment", fetch = FetchType.LAZY)
    private List<UserSettings> userSettings;

    @ManyToMany(mappedBy = "treatments", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EssentialContact> essentialContacts;

    @OneToMany(mappedBy = "treatment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FaqItem> items;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<TreatmentQuestion> treatmentQuestions;

    public Treatment() {
        this.items = new ArrayList<>();
        this.treatmentQuestions = new ArrayList<>();
    }

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
