package br.ufrgs.inf.pet.dinoapi.model.faq;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static br.ufrgs.inf.pet.dinoapi.constants.FaqConstants.*;

public class FaqSaveRequestItemModel {

    @NotNull(message = QUESTION_NULL_MESSAGE)
    @Size(min = 1, max = QUESTION_MAX, message = QUESTION_MESSAGE)
    private String question;

    @NotNull(message = ANSWER_NULL_MESSAGE)
    @Size(min = 1, max = ANSWER_MAX, message = ANSWER_MESSAGE)
    private String answer;

    public FaqSaveRequestItemModel() {}

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

    @JsonIgnore
    public Boolean isValid() {
        return this.question != null && this.answer != null;
    }
}
