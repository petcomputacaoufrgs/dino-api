package br.ufrgs.inf.pet.dinoapi.websocket.model.alert_update.note;

import java.util.List;

public class ColumnOrderUpdateModel {
    private List<ColumnOrderItemUpdateModel> items;
    private Long newVersion;

    public List<ColumnOrderItemUpdateModel> getItems() {
        return items;
    }

    public void setItems(List<ColumnOrderItemUpdateModel> items) {
        this.items = items;
    }

    public Long getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(Long newVersion) {
        this.newVersion = newVersion;
    }
}
