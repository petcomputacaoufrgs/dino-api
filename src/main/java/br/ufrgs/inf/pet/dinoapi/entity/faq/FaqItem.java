package br.ufrgs.inf.pet.dinoapi.entity.faq;

import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
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
    @JoinColumn(name = "faq_id", nullable = false)
    private Faq faq;

    public FaqItem() {}

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

    public Faq getFaq() {
        return faq;
    }

    public void setFaq(Faq faq) {
        this.faq = faq;
    }
}
