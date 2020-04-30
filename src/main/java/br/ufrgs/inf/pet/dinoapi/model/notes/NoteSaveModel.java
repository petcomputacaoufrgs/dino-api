package br.ufrgs.inf.pet.dinoapi.model.notes;

import java.util.List;

public class NoteSaveModel {
    Integer order;

    String question;

    List<Long> tagIdList;

    List<String> newTags;

    Integer lastUpdateDay;

    Integer lastUpdateMonth;

    Integer lastUpdateYear;

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

    public List<Long> getTagIdList() {
        return tagIdList;
    }

    public void setTagIdList(List<Long> tagIdList) {
        this.tagIdList = tagIdList;
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

    public List<String> getNewTags() {
        return newTags;
    }

    public void setNewTags(List<String> newTags) {
        this.newTags = newTags;
    }
}
