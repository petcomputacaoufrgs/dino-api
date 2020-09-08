package br.ufrgs.inf.pet.dinoapi.model.notes;

import br.ufrgs.inf.pet.dinoapi.entity.note.NoteColumn;

public class NoteColumnSaveResponseModel {
    private Long version;

    private Long id;

    public NoteColumnSaveResponseModel() {}

    public NoteColumnSaveResponseModel(Long version, NoteColumn noteColumn) {
        this.version = version;
        this.id = noteColumn.getId();
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
