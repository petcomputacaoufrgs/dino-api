package br.ufrgs.inf.pet.dinoapi.model.notes;

import javax.validation.constraints.Size;

public class NoteUpdateRequestModel extends NoteQuestionRequestModel {

    @Size(max = 500, message ="answer should not be more than 10000.")
    private String answer;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

}
