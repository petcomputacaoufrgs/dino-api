package br.ufrgs.inf.pet.dinoapi.model.notes;

import br.ufrgs.inf.pet.dinoapi.entity.Note;

import java.util.List;
import java.util.stream.Collectors;

public class NoteModel {
    Long id;

    Integer order;

    String question;

    String answer;

    Boolean answered;

    List<NoteTagModel> tags;

    Integer lastUpdateDay;

    Integer lastUpdateMonth;

    Integer lastUpdateYear;

    public NoteModel() {}

    public NoteModel(Note note) {
        this.id = note.getId();
        this.order = note.getOrder();
        this.question = note.getQuestion();
        this.answer = note.getAnswer();
        this.answered = note.getAnswered();
        this.tags = note.getTags().stream().map(tag -> new NoteTagModel(tag)).collect(Collectors.toList());
        this.lastUpdateDay = note.getLastUpdateDay();
        this.lastUpdateMonth = note.getLastUpdateMonth();
        this.lastUpdateYear = note.getLastUpdateYear();
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

    public List<NoteTagModel> getTags() {
        return tags;
    }

    public void setTags(List<NoteTagModel> tags) {
        this.tags = tags;
    }

    public Integer getLastUpdateDay() {
        return lastUpdateDay;
    }

    public void setLastUpdateDay(Integer lastUpdateDay) {
        this.lastUpdateDay = lastUpdateDay;
    }

    public Integer getLastUpdateMonth() {
        return lastUpdateMonth;
    }

    public void setLastUpdateMonth(Integer lastUpdateMonth) {
        this.lastUpdateMonth = lastUpdateMonth;
    }

    public Integer getLastUpdateYear() {
        return lastUpdateYear;
    }

    public void setLastUpdateYear(Integer lastUpdateYear) {
        this.lastUpdateYear = lastUpdateYear;
    }

}
