package br.ufrgs.inf.pet.dinoapi.model.note.sync.column;

import br.ufrgs.inf.pet.dinoapi.constants.NoteColumnConstants;
import br.ufrgs.inf.pet.dinoapi.model.note.save.NoteColumnSaveRequestModel;

import javax.validation.constraints.NotNull;

public class NoteColumnSyncChangedRequestModel extends NoteColumnSaveRequestModel {

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
