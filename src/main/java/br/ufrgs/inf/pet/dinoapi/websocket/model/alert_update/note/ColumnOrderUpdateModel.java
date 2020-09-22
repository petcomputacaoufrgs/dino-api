package br.ufrgs.inf.pet.dinoapi.websocket.model.alert_update.note;

import java.util.List;

public class ColumnOrderUpdateModel {
    private List<ColumnOrderItemUpdateModel> items;

    public List<ColumnOrderItemUpdateModel> getItems() {
        return items;
    }

    public void setItems(List<ColumnOrderItemUpdateModel> items) {
        this.items = items;
    }
}
