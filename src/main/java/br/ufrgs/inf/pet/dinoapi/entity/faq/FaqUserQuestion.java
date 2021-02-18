package br.ufrgs.inf.pet.dinoapi.entity.faq;

import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;

import javax.persistence.*;

import static br.ufrgs.inf.pet.dinoapi.constants.FaqConstants.USER_QUESTION_MAX;

@Entity
@Table(name = "faq_user_question")
public class FaqUserQuestion extends SynchronizableEntity<Long> {
    @Column(name = "question", length = USER_QUESTION_MAX, nullable = false)
    private String question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faq_id", nullable = false)
    private Faq faq;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    public FaqUserQuestion() { }

    public Faq getFaq() {
        return faq;
    }

    public void setFaq(Faq faq) {
        this.faq = faq;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
