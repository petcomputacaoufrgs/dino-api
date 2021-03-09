package br.ufrgs.inf.pet.dinoapi.entity.faq;

import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.entity.treatment.Treatment;

import javax.persistence.*;

import static br.ufrgs.inf.pet.dinoapi.constants.FaqConstants.ANSWER_MAX;
import static br.ufrgs.inf.pet.dinoapi.constants.FaqConstants.QUESTION_MAX;

@Entity
@Table(name = "faq_item")
public class FaqItem extends SynchronizableEntity<Long> {

    @Column(name = "question", length = QUESTION_MAX, nullable = false)
    private String question;

    @Column(name = "answer", length = ANSWER_MAX, nullable = false)
    private String answer;

    @ManyToOne
    @JoinColumn(name = "treatment_id", nullable = false)
    private Treatment treatment;

    public FaqItem() {
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Treatment getTreatment() {
        return treatment;
    }

    public void setTreatment(Treatment treatment) {
        this.treatment = treatment;
    }
}
