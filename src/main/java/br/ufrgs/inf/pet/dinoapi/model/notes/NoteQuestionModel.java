package br.ufrgs.inf.pet.dinoapi.model.notes;

import java.util.List;

public class NoteQuestionModel {
    Long id;

    String question;

    List<NoteTagModel> tagList;

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

    public List<NoteTagModel> getTagList() {
        return tagList;
    }

    public void setTagList(List<NoteTagModel> tagList) {
        this.tagList = tagList;
    }
}
