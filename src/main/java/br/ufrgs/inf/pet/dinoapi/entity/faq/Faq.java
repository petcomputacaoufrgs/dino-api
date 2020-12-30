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

    public Faq() {
        this.items = new ArrayList<>();
        this.faqUserQuestions = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<FaqItem> getItems() {
        return items;
    }

    public void setItems(List<FaqItem> items) {
        this.items = items;
    }

    public List<FaqUserQuestion> getFaqUserQuestions() {
        return faqUserQuestions;
    }

    public void setFaqUserQuestions(List<FaqUserQuestion> faqFaqUserQuestions) {
        this.faqUserQuestions = faqFaqUserQuestions;
    }

    public Treatment getTreatment() {
        return treatment;
    }

    public void setTreatment(Treatment treatment) {
        this.treatment = treatment;
    }
}
