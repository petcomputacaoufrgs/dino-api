package br.ufrgs.inf.pet.dinoapi.model.faq;

import br.ufrgs.inf.pet.dinoapi.constants.FaqConstants;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataLocalIdModel;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class FaqItemDataModel extends SynchronizableDataLocalIdModel<Long, Integer> {
    @NotNull(message = FaqConstants.QUESTION_NULL_MESSAGE)
    @Size(min = FaqConstants.QUESTION_MIN, max = FaqConstants.QUESTION_MAX, message = FaqConstants.QUESTION_SIZE_MESSAGE)
    private String question;

    @NotNull(message = FaqConstants.ANSWER_NULL_MESSAGE)
    @Size(min = FaqConstants.ANSWER_MIN, max = FaqConstants.ANSWER_MAX, message = FaqConstants.ANSWER_MESSAGE)
    private String answer;

    @NotNull(message = FaqConstants.FAQ_ID_NULL_MESSAGE)
    private Long faqId;

    public FaqItemDataModel() {}

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

    public Long getFaqId() {
        return faqId;
    }

    public void setFaqId(Long faqId) {
        this.faqId = faqId;
    }
}
