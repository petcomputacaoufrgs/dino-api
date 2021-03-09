package br.ufrgs.inf.pet.dinoapi.entity.treatment;

import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;
import br.ufrgs.inf.pet.dinoapi.entity.user.User;

import javax.persistence.*;

import static br.ufrgs.inf.pet.dinoapi.constants.FaqConstants.TREATMENT_USER_QUESTION_MAX;

@Entity
@Table(name = "treatment_question")
public class TreatmentQuestion extends SynchronizableEntity<Long> {
    @Column(name = "question", length = TREATMENT_USER_QUESTION_MAX, nullable = false)
    private String question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treatment_id", nullable = false)
    private Treatment treatment;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    public TreatmentQuestion() {
    }

    public Treatment getTreatment() {
        return treatment;
    }

    public void setTreatment(Treatment treatment) {
        this.treatment = treatment;
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
