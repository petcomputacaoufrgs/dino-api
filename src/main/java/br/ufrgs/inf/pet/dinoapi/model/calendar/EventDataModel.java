package br.ufrgs.inf.pet.dinoapi.model.calendar;

import br.ufrgs.inf.pet.dinoapi.constants.CalendarConstants;
import br.ufrgs.inf.pet.dinoapi.constants.ContactsConstants;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataLocalIdModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EventDataModel extends SynchronizableDataLocalIdModel<Long> {

    @NotNull(message = ContactsConstants.NAME_NULL_MESSAGE)
    @Size(max = ContactsConstants.NAME_MAX, message = ContactsConstants.NAME_MAX_MESSAGE)
    private String title;

    @Size(max = ContactsConstants.DESCRIPTION_MAX, message = ContactsConstants.DESCRIPTION_MAX_MESSAGE)
    private String description;

    @Size(max = ContactsConstants.NAME_MAX, message = ContactsConstants.DESCRIPTION_MAX_MESSAGE)
    private String date;

    @Size(max = CalendarConstants.TIME_MAX, message = ContactsConstants.DESCRIPTION_MAX_MESSAGE)
    private String endTime;

    @NotNull(message = ContactsConstants.NAME_NULL_MESSAGE)
    private Long typeId;

    public EventDataModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }
}
