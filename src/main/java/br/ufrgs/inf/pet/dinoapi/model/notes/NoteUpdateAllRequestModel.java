package br.ufrgs.inf.pet.dinoapi.model.notes;

import javax.validation.Valid;
import java.util.List;

public class NoteUpdateAllRequestModel {
    @Valid
    private List<NoteSaveRequestModel> items;

    public List<NoteSaveRequestModel> getItems() {
        return items;
    }

    public void setItems(List<NoteSaveRequestModel> items) {
        this.items = items;
    }
}
