package br.ufrgs.inf.pet.dinoapi.model.notes;

import br.ufrgs.inf.pet.dinoapi.entity.note.Note;

public class NoteSaveResponseModel {
    private Long userNoteVersion;

    private Long id;

    public NoteSaveResponseModel() {}

    public NoteSaveResponseModel(Long userNoteVersion, Note note) {
        this.userNoteVersion = userNoteVersion;
        this.id = note.getId();
    }

    public Long getUserNoteVersion() {
        return userNoteVersion;
    }

    public void setUserNoteVersion(Long userNoteVersion) {
        this.userNoteVersion = userNoteVersion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
