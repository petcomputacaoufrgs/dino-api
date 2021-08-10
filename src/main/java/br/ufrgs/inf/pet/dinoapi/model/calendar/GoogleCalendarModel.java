package br.ufrgs.inf.pet.dinoapi.model.calendar;

import javax.validation.constraints.NotBlank;

public class GoogleCalendarModel {

    private String id;

    @NotBlank
    private String summary;

    private String description;

    private String timeZone;

    public GoogleCalendarModel(@NotBlank String summary) {
        this.summary = summary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
