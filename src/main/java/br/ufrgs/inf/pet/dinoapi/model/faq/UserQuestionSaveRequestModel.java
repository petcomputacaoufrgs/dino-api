package br.ufrgs.inf.pet.dinoapi.model.faq;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static br.ufrgs.inf.pet.dinoapi.constants.FaqConstants.*;

public class UserQuestionSaveRequestModel {

    @NotNull(message = ID_NULL_MESSAGE)
    private Long id;

    @NotNull(message = QUESTION_NULL_MESSAGE)
    @Size(min = 1, max = USER_QUESTION_MAX, message = QUESTION_MESSAGE)
    private String question;

    public UserQuestionSaveRequestModel() {}

    public Long getId() {
        return id;
    }

    public void setId(Long faqId) {
        this.id = faqId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
