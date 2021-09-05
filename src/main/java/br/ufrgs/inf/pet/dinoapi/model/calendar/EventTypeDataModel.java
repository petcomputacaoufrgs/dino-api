package br.ufrgs.inf.pet.dinoapi.model.calendar;

import br.ufrgs.inf.pet.dinoapi.constants.ContactsConstants;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataLocalIdModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EventTypeDataModel extends SynchronizableDataLocalIdModel<Long> {
    @NotNull(message = ContactsConstants.NAME_NULL_MESSAGE)
    @Size(max = ContactsConstants.NAME_MAX, message = ContactsConstants.NAME_MAX_MESSAGE)
    private String title;

    @Size(max = ContactsConstants.DESCRIPTION_MAX, message = ContactsConstants.DESCRIPTION_MAX_MESSAGE)
    private String color;

    @Size(max = ContactsConstants.DESCRIPTION_MAX, message = ContactsConstants.DESCRIPTION_MAX_MESSAGE)
    private String icon;
}
