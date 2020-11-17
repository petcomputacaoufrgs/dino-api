package br.ufrgs.inf.pet.dinoapi.entity.faq;

import br.ufrgs.inf.pet.dinoapi.entity.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static br.ufrgs.inf.pet.dinoapi.constants.FaqConstants.USER_QUESTION_MAX;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "faq_user_question")
public class FaqUserQuestion implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String SEQUENCE_NAME = "user_question_seq";

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faq_id", nullable = false)
    private Faq faq;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "question", length = USER_QUESTION_MAX, nullable = false)
    private String question;

    @Column(name = "date", nullable = false)
    private Date date;

    public FaqUserQuestion() {}

    public FaqUserQuestion(Faq faq, User user, String question, Date date) {
        this.faq = faq;
        this.user = user;
        this.question = question;
        this.date = date;
    }

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
