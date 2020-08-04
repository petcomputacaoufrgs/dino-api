package br.ufrgs.inf.pet.dinoapi.model.notes;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class NoteAnswerRequestModel {

    @NotNull(message = "id cannot be null.")
    private Long id;

    @NotNull(message = "answer cannot be null.")
    @Size(max = 1000, message = "answer should not be more than 10000.")
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
