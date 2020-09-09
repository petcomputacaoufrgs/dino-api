package br.ufrgs.inf.pet.dinoapi.model.notes;

import javax.validation.constraints.Size;

import static br.ufrgs.inf.pet.dinoapi.constants.NoteConstants.ANSWER_MAX;
import static br.ufrgs.inf.pet.dinoapi.constants.NoteConstants.ANSWER_MESSAGE;

public class NoteUpdateRequestModel extends NoteQuestionRequestModel {

    @Size(max = ANSWER_MAX, message = ANSWER_MESSAGE)
    private String answer;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

}
