package br.ufrgs.inf.pet.dinoapi.entity.faq;

import br.ufrgs.inf.pet.dinoapi.model.faq.FaqItemModel;
import br.ufrgs.inf.pet.dinoapi.model.faq.FaqSaveRequestItemModel;

import javax.persistence.*;
import java.io.Serializable;

import static br.ufrgs.inf.pet.dinoapi.constants.FaqConstants.ANSWER_MAX;
import static br.ufrgs.inf.pet.dinoapi.constants.FaqConstants.QUESTION_MAX;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "faq_item")
public class FaqItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String SEQUENCE_NAME = "faq_item_seq";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "question", length = QUESTION_MAX, nullable = false)
    private String question;

    @Column(name = "answer", length = ANSWER_MAX, nullable = false)
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faq_id", nullable = false)
    private Faq faq;

    public FaqItem() {}

    public FaqItem(FaqItemModel faqItemModel, Faq faqItem) {
        this.setQuestion(faqItemModel.getQuestion());
        this.setAnswer(faqItemModel.getAnswer());
        this.setFaq(faqItem);
    }

    public FaqItem(FaqSaveRequestItemModel item, Faq faq) {
        this.setQuestion(item.getQuestion());
        this.setAnswer(item.getAnswer());
        this.setFaq(faq);
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

    public Faq getFaq() {
        return faq;
    }

    public void setFaq(Faq faq) {
        this.faq = faq;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
