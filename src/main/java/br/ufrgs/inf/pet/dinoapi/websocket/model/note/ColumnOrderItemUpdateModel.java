package br.ufrgs.inf.pet.dinoapi.websocket.model.note;

import br.ufrgs.inf.pet.dinoapi.entity.note.NoteColumn;

public class ColumnOrderItemUpdateModel {
    private Long id;
    private Long lastOrderUpdate;
    private Integer order;

    public ColumnOrderItemUpdateModel(NoteColumn noteColumn) {
        this.id = noteColumn.getId();
        this.order = noteColumn.getOrder();
        this.lastOrderUpdate = noteColumn.getLastOrderUpdate().getTime();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Long getLastOrderUpdate() {
        return lastOrderUpdate;
    }

    public void setLastOrderUpdate(Long lastOrderUpdate) {
        this.lastOrderUpdate = lastOrderUpdate;
    }
}
