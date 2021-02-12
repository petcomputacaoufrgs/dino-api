package br.ufrgs.inf.pet.dinoapi.entity.faq;

import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.entity.treatment.Treatment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static br.ufrgs.inf.pet.dinoapi.constants.FaqConstants.TITLE_MAX;

@Entity
@Table(name = "faq")
public class Faq extends SynchronizableEntity<Long> {

    @Column(name = "title", length = TITLE_MAX, nullable = false, unique = true)
    private String title;

    @OneToOne
    @JoinColumn(name = "treatment_id", referencedColumnName = "id", nullable = false)
    private Treatment treatment;

    @OneToMany(mappedBy = "faq", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FaqItem> items;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<FaqUserQuestion> faqUserQuestions;

    public Faq() { }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Treatment getTreatment() {
        return treatment;
    }

    public void setTreatment(Treatment treatment) {
        this.treatment = treatment;
    }
}
