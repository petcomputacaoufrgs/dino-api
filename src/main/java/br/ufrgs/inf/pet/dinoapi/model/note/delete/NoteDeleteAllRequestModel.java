package br.ufrgs.inf.pet.dinoapi.model.note.delete;

import javax.validation.Valid;
import java.util.List;

public class NoteDeleteAllRequestModel {
    @Valid
    private List<NoteDeleteRequestModel> items;

    public List<NoteDeleteRequestModel> getItems() {
        return items;
    }

    public void setItems(List<NoteDeleteRequestModel> items) {
        this.items = items;
    }
}
