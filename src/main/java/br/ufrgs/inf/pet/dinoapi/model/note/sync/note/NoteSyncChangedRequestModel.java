package br.ufrgs.inf.pet.dinoapi.model.note.sync.note;

import br.ufrgs.inf.pet.dinoapi.constants.NoteConstants;

import javax.validation.constraints.NotNull;

public class NoteSyncChangedRequestModel extends NoteSyncSaveRequestModel {
    @NotNull(message = NoteConstants.ID_NULL_MESSAGE)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
