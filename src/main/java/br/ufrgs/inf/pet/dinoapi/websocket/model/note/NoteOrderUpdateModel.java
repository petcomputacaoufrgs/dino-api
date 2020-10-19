package br.ufrgs.inf.pet.dinoapi.websocket.model.note;

import java.util.List;

public class NoteOrderUpdateModel {
    private List<NoteOrderItemUpdateModel> items;

    public List<NoteOrderItemUpdateModel> getItems() {
        return items;
    }

    public void setItems(List<NoteOrderItemUpdateModel> items) {
        this.items = items;
    }
}
