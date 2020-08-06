package br.ufrgs.inf.pet.dinoapi.model.notes;

public class NoteSaveResponseModel {
    private Long version;

    private Long noteId;

    public NoteSaveResponseModel() {}

    public NoteSaveResponseModel(Long version, Long noteId) {
        this.version = version;
        this.noteId = noteId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getNoteId() {
        return noteId;
    }

    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }
}
