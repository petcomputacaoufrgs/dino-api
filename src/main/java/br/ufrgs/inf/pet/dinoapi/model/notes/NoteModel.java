package br.ufrgs.inf.pet.dinoapi.model.notes;

import br.ufrgs.inf.pet.dinoapi.entity.Note;
import br.ufrgs.inf.pet.dinoapi.utils.DatetimeUtils;

import java.util.List;
import java.util.stream.Collectors;

public class NoteModel {
    Long id;

    Integer order;

    String question;

    String answer;

    Boolean answered;

    List<String> tags;

    Long lastUpdate;

    public NoteModel() {}

    public NoteModel(Note note) {
        this.id = note.getId();
        this.order = note.getOrder();
        this.question = note.getQuestion();
        this.answer = note.getAnswer();
        this.answered = note.getAnswered();
        this.tags = note.getTags().stream().map(tag -> tag.getName()).collect(Collectors.toList());
        this.lastUpdate = DatetimeUtils.convertLocalDatetimeToMilliseconds(note.getLastUpdate());
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

    public Boolean getAnswered() {
        return answered;
    }

    public void setAnswered(Boolean answered) {
        this.answered = answered;
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
}
