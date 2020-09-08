package br.ufrgs.inf.pet.dinoapi.model.notes;

import br.ufrgs.inf.pet.dinoapi.entity.note.Note;

public class NoteSaveResponseModel extends NoteSaveRequestModel {
    private Long userNoteVersion;

    private Long id;

    public NoteSaveResponseModel() {}

    public NoteSaveResponseModel(Long userNoteVersion, Note note) {
        this.userNoteVersion = userNoteVersion;
        this.id = note.getId();
    }

    public Long getNoteVersion() {
        return userNoteVersion;
    }

    public void setNoteVersion(Long noteVersion) {
        this.userNoteVersion = noteVersion;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
