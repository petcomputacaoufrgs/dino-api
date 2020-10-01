package br.ufrgs.inf.pet.dinoapi.model.notes;

import br.ufrgs.inf.pet.dinoapi.constants.NoteColumnConstants;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

public class NoteColumnUpdateAllRequestModel {
    @Valid
    @Size(max= NoteColumnConstants.MAX_COLUMNS, message = NoteColumnConstants.MAX_COLUMNS_MESSAGE)
    private List<NoteColumnSaveRequestModel> items;

    public List<NoteColumnSaveRequestModel> getItems() {
        return items;
    }

    public void setItems(List<NoteColumnSaveRequestModel> items) {
        this.items = items;
    }
}
