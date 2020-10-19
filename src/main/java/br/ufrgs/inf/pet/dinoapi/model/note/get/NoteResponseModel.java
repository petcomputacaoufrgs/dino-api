package br.ufrgs.inf.pet.dinoapi.model.note.get;

import br.ufrgs.inf.pet.dinoapi.entity.note.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NoteResponseModel {
    private Long id;

    private Integer order;

    private String question;

    private String answer;

    private List<String> tags;

    private Long lastUpdate;

    private Long lastOrderUpdate;

    private String columnTitle;

    public NoteResponseModel() {
        this.tags = new ArrayList<>();
    }

    public NoteResponseModel(Note note) {
        this.id = note.getId();
        this.order = note.getOrder();
        this.question = note.getQuestion();
        this.answer = note.getAnswer();
        this.tags = note.getTags().stream().map(tag -> tag.getName()).collect(Collectors.toList());
        this.lastUpdate = note.getLastUpdate().getTime();
        this.columnTitle = note.getNoteColumn().getTitle();
        this.lastOrderUpdate = note.getLastOrderUpdate().getTime();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
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
