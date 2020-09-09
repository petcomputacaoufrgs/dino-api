package br.ufrgs.inf.pet.dinoapi.model.notes;

import javax.validation.Valid;
import java.util.List;

public class NoteOrderAllRequestModel {
    @Valid
    private List<NoteOrderRequestModel> items;

    public List<NoteOrderRequestModel> getItems() {
        return items;
    }

    public void setItems(List<NoteOrderRequestModel> items) {
        this.items = items;
    }
}
