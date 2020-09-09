package br.ufrgs.inf.pet.dinoapi.model.notes;

import javax.validation.Valid;
import java.util.List;

public class NoteColumnOrderAllRequestModel {
    @Valid
    private List<NoteColumnOrderRequestModel> items;

    public List<NoteColumnOrderRequestModel> getItems() {
        return items;
    }

    public void setItems(List<NoteColumnOrderRequestModel> items) {
        this.items = items;
    }
}
