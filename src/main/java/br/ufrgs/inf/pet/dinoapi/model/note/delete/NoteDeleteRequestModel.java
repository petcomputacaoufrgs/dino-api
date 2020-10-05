package br.ufrgs.inf.pet.dinoapi.model.note.delete;

import javax.validation.constraints.NotNull;

import static br.ufrgs.inf.pet.dinoapi.constants.NoteConstants.ID_NULL_MESSAGE;

public class NoteDeleteRequestModel {
    @NotNull(message = ID_NULL_MESSAGE)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
