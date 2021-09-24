package br.ufrgs.inf.pet.dinoapi.model.google.calendar;

import com.google.api.client.util.DateTime;

public class GoogleEventDateTimeModel {
    private DateTime datetime;
    private String timeZone;

    public DateTime getDateTime() {
        return datetime;
    }

    public void setDateTime(DateTime datetime) {
        this.datetime = datetime;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
