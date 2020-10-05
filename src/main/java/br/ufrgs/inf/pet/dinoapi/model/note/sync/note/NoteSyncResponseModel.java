package br.ufrgs.inf.pet.dinoapi.model.note.sync.note;

import br.ufrgs.inf.pet.dinoapi.model.note.get.NoteResponseModel;

import java.util.List;

public class NoteSyncResponseModel {
    private List<NoteResponseModel> notes;

    private Long version;

    public List<NoteResponseModel> getNotes() {
        return notes;
    }

    public void setNotes(List<NoteResponseModel> notes) {
        this.notes = notes;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
