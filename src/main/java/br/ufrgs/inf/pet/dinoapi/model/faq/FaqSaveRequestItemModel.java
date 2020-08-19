package br.ufrgs.inf.pet.dinoapi.model.faq;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class FaqSaveRequestItemModel {
    private String question;
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
