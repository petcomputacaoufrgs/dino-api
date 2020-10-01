package br.ufrgs.inf.pet.dinoapi.model.notes;

import br.ufrgs.inf.pet.dinoapi.constants.NoteConstants;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class NoteSaveRequestModel {

    protected Long id;

    @NotNull(message = NoteConstants.QUESTION_NULL_MESSAGE)
    @Size(min=NoteConstants.QUESTION_MIN, max = NoteConstants.QUESTION_MAX, message = NoteConstants.QUESTION_SIZE_MESSAGE)
    protected String question;

    @Valid
    @Size(max = NoteConstants.MAX_TAGS, message = NoteConstants.MAX_TAGS_MESSAGE)
    protected List<String> tagNames;

    @NotNull(message = NoteConstants.LAST_UPDATE_NULL_MESSAGE)
    protected Long lastUpdate;

    protected Long lastOrderUpdate;

    @Size(max = NoteConstants.ANSWER_MAX, message = NoteConstants.ANSWER_SIZE_MESSAGE)
    protected String answer;

    protected Integer order;

    @NotNull(message = NoteConstants.COLUMN_TITLE_NULL_MESSAGE)
    private String columnTitle;

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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getColumnTitle() {
        return columnTitle;
    }

    public void setColumnTitle(String columnTitle) {
        this.columnTitle = columnTitle;
    }

    public Long getLastOrderUpdate() {
        return lastOrderUpdate;
    }

    public void setLastOrderUpdate(Long lastOrderUpdate) {
        this.lastOrderUpdate = lastOrderUpdate;
    }
}
