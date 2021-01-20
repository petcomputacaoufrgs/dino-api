package br.ufrgs.inf.pet.dinoapi.model.faq;

import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataLocalIdModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static br.ufrgs.inf.pet.dinoapi.constants.FaqConstants.*;

public class FaqUserQuestionDataModel extends SynchronizableDataLocalIdModel<Long> {
    @NotNull(message = ID_NULL_MESSAGE)
    private Long faqId;

    @NotNull(message = QUESTION_NULL_MESSAGE)
    @Size(min = 1, max = USER_QUESTION_MAX, message = QUESTION_SIZE_MESSAGE)
    private String question;

    public FaqUserQuestionDataModel() {
    }

    public Long getFaqId() {
        return faqId;
    }

    public void setFaqId(Long faqId) {
        this.faqId = faqId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
