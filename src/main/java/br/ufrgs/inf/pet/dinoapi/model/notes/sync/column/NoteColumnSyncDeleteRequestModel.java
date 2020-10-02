package br.ufrgs.inf.pet.dinoapi.model.notes.sync.column;

import br.ufrgs.inf.pet.dinoapi.constants.NoteColumnConstants;
import br.ufrgs.inf.pet.dinoapi.model.notes.NoteColumnDeleteRequestModel;

import javax.validation.constraints.NotNull;

public class NoteColumnSyncDeleteRequestModel extends NoteColumnDeleteRequestModel {
    @NotNull(message = NoteColumnConstants.LAST_UPDATE_NULL_MESSAGE)
    private Long lastUpdate;

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
