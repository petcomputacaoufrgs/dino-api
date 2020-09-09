package br.ufrgs.inf.pet.dinoapi.model.notes;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

import static br.ufrgs.inf.pet.dinoapi.constants.NoteConstants.*;

public class NoteQuestionRequestModel {

    @NotNull(message = ID_NULL_MESSAGE)
    private Long id;

    @NotNull(message = QUESTION_NULL_MESSAGE)
    @Size(max = 500, message = QUESTION_MESSAGE)
    private String question;

    @Valid
    private List<String> tagNames;

    @NotNull(message = "lastUpdate cannot be null.")
    private Long lastUpdate;

    @NotNull(message = "answered cannot be null.")
    private Boolean answered;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getTagNames() {
        return tagNames;
    }

    public void setTagNames(List<String> tagNames) {
        this.tagNames = tagNames;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Boolean getAnswered() {
        return answered;
    }

    public void setAnswered(Boolean answered) {
        this.answered = answered;
    }
}
