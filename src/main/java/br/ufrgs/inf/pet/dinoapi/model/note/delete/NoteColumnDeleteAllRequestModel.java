package br.ufrgs.inf.pet.dinoapi.model.note.delete;

import javax.validation.Valid;
import java.util.List;

public class NoteColumnDeleteAllRequestModel {
    @Valid
    private List<NoteColumnDeleteRequestModel> items;

    public List<NoteColumnDeleteRequestModel> getItems() {
        return items;
    }

    public void setItems(List<NoteColumnDeleteRequestModel> items) {
        this.items = items;
    }
}
