package br.ufrgs.inf.pet.dinoapi.model.notes;

import br.ufrgs.inf.pet.dinoapi.entity.note.NoteColumn;

public class NoteColumnItemUpdateAllResponseModel {
    private String title;
    private Long id;

    public NoteColumnItemUpdateAllResponseModel(NoteColumn noteColumn) {
        this.title = noteColumn.getTitle();
        this.id = noteColumn.getId();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
