package br.ufrgs.inf.pet.dinoapi.model.contacts;

import javax.validation.constraints.NotNull;

import static br.ufrgs.inf.pet.dinoapi.constants.ContactsConstants.ID_NULL_MESSAGE;

public class ContactIdModel {

    @NotNull(message = ID_NULL_MESSAGE)
    Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

