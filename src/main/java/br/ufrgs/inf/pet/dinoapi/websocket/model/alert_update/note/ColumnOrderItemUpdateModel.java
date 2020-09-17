package br.ufrgs.inf.pet.dinoapi.websocket.model.alert_update.note;

import br.ufrgs.inf.pet.dinoapi.entity.note.NoteColumn;

public class ColumnOrderItemUpdateModel {
    private String title;
    private Integer order;

    public ColumnOrderItemUpdateModel(NoteColumn noteColumn) {
        this.title = noteColumn.getTitle();
        this.order = noteColumn.getOrder();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
