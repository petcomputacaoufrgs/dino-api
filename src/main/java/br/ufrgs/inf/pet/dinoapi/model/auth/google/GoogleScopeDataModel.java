package br.ufrgs.inf.pet.dinoapi.model.auth.google;

import br.ufrgs.inf.pet.dinoapi.constants.ContactsConstants;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataLocalIdModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class GoogleScopeDataModel extends SynchronizableDataLocalIdModel<Long, Integer> {
    @NotNull(message = ContactsConstants.NAME_NULL_MESSAGE)
    @Size(max = ContactsConstants.NAME_MAX, message =ContactsConstants.NAME_MAX_MESSAGE)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}