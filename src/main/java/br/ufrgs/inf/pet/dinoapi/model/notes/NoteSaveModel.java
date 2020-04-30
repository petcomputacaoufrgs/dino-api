package br.ufrgs.inf.pet.dinoapi.model.notes;

import java.util.List;

public class NoteSaveModel {
    Integer order;

    String question;

    List<String> tagNames;

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

    public List<String> getTagNames() {
        return tagNames;
    }

    public void setTagNames(List<String> tagNames) {
        this.tagNames = tagNames;
    }
}
