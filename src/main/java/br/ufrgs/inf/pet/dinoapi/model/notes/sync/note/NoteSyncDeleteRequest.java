package br.ufrgs.inf.pet.dinoapi.model.notes.sync.note;

import br.ufrgs.inf.pet.dinoapi.constants.NoteConstants;
import br.ufrgs.inf.pet.dinoapi.model.notes.NoteDeleteRequestModel;

import javax.validation.constraints.NotNull;

public class NoteSyncDeleteRequest extends NoteDeleteRequestModel {
    @NotNull(message = NoteConstants.LAST_UPDATE_NULL_MESSAGE)
    private Long lastUpdate;

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
