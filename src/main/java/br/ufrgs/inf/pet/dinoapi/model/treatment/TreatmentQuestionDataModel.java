package br.ufrgs.inf.pet.dinoapi.model.treatment;

import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataLocalIdModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static br.ufrgs.inf.pet.dinoapi.constants.FaqConstants.*;

public class TreatmentQuestionDataModel extends SynchronizableDataLocalIdModel<Long> {
    @NotNull(message = ID_NULL_MESSAGE)
    private Long treatmentId;

    @NotNull(message = QUESTION_NULL_MESSAGE)
    @Size(min = 1, max = TREATMENT_USER_QUESTION_MAX, message = QUESTION_SIZE_MESSAGE)
    private String question;

    public TreatmentQuestionDataModel() {
    }

    public Long getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(Long treatmentId) {
        this.treatmentId = treatmentId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
