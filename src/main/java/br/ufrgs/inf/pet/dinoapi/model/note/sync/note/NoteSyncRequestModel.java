package br.ufrgs.inf.pet.dinoapi.model.note.sync.note;

import br.ufrgs.inf.pet.dinoapi.constants.NoteConstants;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public class NoteSyncRequestModel {
    @Valid
    @NotNull(message = NoteConstants.CHANGED_NOTES_NULL_MESSAGE)
    private List<NoteSyncChangedRequestModel> changedNotes;

    @Valid
    @NotNull(message = NoteConstants.NEW_NOTES_NULL_MESSAGE)
    private List<NoteSyncSaveRequestModel> newNotes;

    @Valid
    @NotNull(message = NoteConstants.DELETED_NOTES_NULL_MESSAGE)
    private List<NoteSyncDeleteRequest> deletedNotes;

    @Valid
    @NotNull(message = NoteConstants.NOTE_ORDER_NULL_MESSAGE)
    private List<NoteSyncOrderRequestModel> notesOrder;

    public List<NoteSyncChangedRequestModel> getChangedNotes() {
        return changedNotes;
    }

    public void setChangedNotes(List<NoteSyncChangedRequestModel> changedNotes) {
        this.changedNotes = changedNotes;
    }

    public List<NoteSyncSaveRequestModel> getNewNotes() {
        return newNotes;
    }

    public void setNewNotes(List<NoteSyncSaveRequestModel> newNotes) {
        this.newNotes = newNotes;
    }

    public List<NoteSyncDeleteRequest> getDeletedNotes() {
        return deletedNotes;
    }

    public void setDeletedNotes(List<NoteSyncDeleteRequest> deletedNotes) {
        this.deletedNotes = deletedNotes;
    }

    public List<NoteSyncOrderRequestModel> getNotesOrder() {
        return notesOrder;
    }

    public void setNotesOrder(List<NoteSyncOrderRequestModel> notesOrder) {
        this.notesOrder = notesOrder;
    }
}
