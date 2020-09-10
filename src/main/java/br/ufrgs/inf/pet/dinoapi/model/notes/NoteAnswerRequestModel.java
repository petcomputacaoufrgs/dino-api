package br.ufrgs.inf.pet.dinoapi.model.notes;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static br.ufrgs.inf.pet.dinoapi.constants.NoteConstants.*;

public class NoteAnswerRequestModel {

    @NotNull(message = ID_NULL_MESSAGE)
    private Long id;

    @NotNull(message = ANSWER_NULL_MESSAGE)
    @Size(max = ANSWER_MAX, message = ANSWER_MESSAGE)
    private String answer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
