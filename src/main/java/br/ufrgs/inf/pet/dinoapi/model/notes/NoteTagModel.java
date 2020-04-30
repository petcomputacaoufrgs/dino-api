package br.ufrgs.inf.pet.dinoapi.model.notes;

import br.ufrgs.inf.pet.dinoapi.entity.NoteTag;

public class NoteTagModel {
    Long id;

    String name;

    public NoteTagModel() {}

    public NoteTagModel(NoteTag tag) {
        this.id = tag.getId();
        this.name = tag.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
