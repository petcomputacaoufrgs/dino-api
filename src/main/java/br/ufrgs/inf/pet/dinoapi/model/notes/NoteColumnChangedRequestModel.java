package br.ufrgs.inf.pet.dinoapi.model.notes;

import br.ufrgs.inf.pet.dinoapi.constants.NoteColumnConstants;

import javax.validation.constraints.NotNull;

public class NoteColumnChangedRequestModel extends NoteColumnSaveRequestModel {

    @NotNull(message = NoteColumnConstants.ID_NULL_MESSAGE)
    private Long id;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
