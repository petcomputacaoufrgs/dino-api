package br.ufrgs.inf.pet.dinoapi.model.calendar;

import br.ufrgs.inf.pet.dinoapi.constants.ContactsConstants;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataLocalIdModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

public class EventDataModel extends SynchronizableDataLocalIdModel<Long> {

    @NotNull(message = ContactsConstants.NAME_NULL_MESSAGE)
    @Size(max = ContactsConstants.NAME_MAX, message = ContactsConstants.NAME_MAX_MESSAGE)
    private String title;

    @Size(max = ContactsConstants.DESCRIPTION_MAX, message = ContactsConstants.DESCRIPTION_MAX_MESSAGE)
    private String description;

    private ZonedDateTime start;

    private ZonedDateTime end;

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

    public ZonedDateTime getStart() {
        return start;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }
}
