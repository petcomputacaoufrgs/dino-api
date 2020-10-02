package br.ufrgs.inf.pet.dinoapi.model.notes;

import javax.validation.Valid;
import java.util.List;

public class NoteUpdateAllRequestModel {
    @Valid
    private List<NoteColumnSaveRequestModel> items;

    public List<NoteColumnSaveRequestModel> getItems() {
        return items;
    }

    public void setItems(List<NoteColumnSaveRequestModel> items) {
        this.items = items;
    }
}
