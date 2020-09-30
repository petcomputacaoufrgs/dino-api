package br.ufrgs.inf.pet.dinoapi.model.notes;

import br.ufrgs.inf.pet.dinoapi.entity.note.NoteColumn;

import java.util.List;
import java.util.stream.Collectors;

public class NoteColumnUpdateAllResponseModel {
    private Long newVersion;
    private List<NoteColumnItemUpdateAllResponseModel> items;

    public NoteColumnUpdateAllResponseModel(List<NoteColumn> noteColumnList, Long newVersion) {
        this.items = noteColumnList.stream().map(NoteColumnItemUpdateAllResponseModel::new).collect(Collectors.toList());
        this.newVersion = newVersion;
    }

    public Long getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(Long newVersion) {
        this.newVersion = newVersion;
    }

    public List<NoteColumnItemUpdateAllResponseModel> getItems() {
        return items;
    }

    public void setItems(List<NoteColumnItemUpdateAllResponseModel> items) {
        this.items = items;
    }
}
