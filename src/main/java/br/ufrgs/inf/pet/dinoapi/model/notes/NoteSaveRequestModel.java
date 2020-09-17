package br.ufrgs.inf.pet.dinoapi.model.notes;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class NoteSaveRequestModel {

    protected Long id;

    @NotNull(message = "question cannot be null.")
    @Size(max = 250, message = "question should not be more than 500.")
    protected String question;

    @Valid
    @Size(max = 10, message = "a note can not have more than 5 tags")
    protected List<@Size(max=50, message = "tag should not be more than 50") String> tagNames;

    @NotNull(message = "lastUpdate cannot be null.")
    protected Long lastUpdate;

    protected Long lastOrderUpdate;

    @Size(max = 500, message ="answer should not be more than 10000.")
    protected String answer;

    protected Integer order;

    @NotNull(message = "columnTitle cannot be null.")
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
