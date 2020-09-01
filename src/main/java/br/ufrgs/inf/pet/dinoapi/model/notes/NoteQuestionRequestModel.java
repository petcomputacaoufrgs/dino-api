package br.ufrgs.inf.pet.dinoapi.model.notes;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class NoteQuestionRequestModel {

    @NotNull(message = "id cannot be null.")
    private Long id;

    @NotNull(message = "question cannot be null.")
    @Size(max = 250, message = "question should not be more than 500.")
    private String question;

    @Valid
    @Size(max = 10, message = "a note can not have more than 5 tags")
    private List<@Size(max=50, message = "tag should not be more than 50") String> tagNames;

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
