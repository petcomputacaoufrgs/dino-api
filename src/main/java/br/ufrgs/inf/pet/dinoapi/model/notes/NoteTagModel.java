package br.ufrgs.inf.pet.dinoapi.model.notes;

import br.ufrgs.inf.pet.dinoapi.entity.NoteTag;

public class NoteTagModel {
    java.lang.Long id;

    String name;

    public NoteTagModel() {}

    public NoteTagModel(NoteTag tag) {
        this.id = tag.getId();
        this.name = tag.getName();
    }

    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
