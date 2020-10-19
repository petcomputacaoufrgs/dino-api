package br.ufrgs.inf.pet.dinoapi.model.note.delete;

import br.ufrgs.inf.pet.dinoapi.constants.NoteColumnConstants;

import javax.validation.constraints.NotNull;

public class NoteColumnDeleteRequestModel {
    @NotNull(message = NoteColumnConstants.ID_NULL_MESSAGE)
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
