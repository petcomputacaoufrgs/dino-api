package br.ufrgs.inf.pet.dinoapi.model.google.calendar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleEventModel {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String summary;
    private String description;
    private GoogleEventDateTimeModel start;
    private GoogleEventDateTimeModel end;
    private List<String> recurrence;


    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GoogleEventDateTimeModel getStart() {
        return start;
    }

    public void setStart(GoogleEventDateTimeModel start) {
        this.start = start;
    }

    public GoogleEventDateTimeModel getEnd() {
        return end;
    }

    public void setEnd(GoogleEventDateTimeModel end) {
        this.end = end;
    }

    public List<String> getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(List<String> recurrence) {
        this.recurrence = recurrence;
    }
}
