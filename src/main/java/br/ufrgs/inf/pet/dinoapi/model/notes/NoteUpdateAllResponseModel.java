package br.ufrgs.inf.pet.dinoapi.model.notes;

import br.ufrgs.inf.pet.dinoapi.entity.note.Note;

import java.util.List;
import java.util.stream.Collectors;

public class NoteUpdateAllResponseModel {
    private Long newVersion;
    private List<NoteUpdateAllItemResponseModel> items;

    public NoteUpdateAllResponseModel(List<Note> notes, Long newVersion) {
        this.items = notes.stream().map(NoteUpdateAllItemResponseModel::new).collect(Collectors.toList());
        this.newVersion = newVersion;
    }

    public Long getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(Long newVersion) {
        this.newVersion = newVersion;
    }

    public List<NoteUpdateAllItemResponseModel> getItems() {
        return items;
    }

    public void setItems(List<NoteUpdateAllItemResponseModel> items) {
        this.items = items;
    }
}
