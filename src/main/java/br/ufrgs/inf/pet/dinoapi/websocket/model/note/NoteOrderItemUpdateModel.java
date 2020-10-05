package br.ufrgs.inf.pet.dinoapi.websocket.model.note;

import br.ufrgs.inf.pet.dinoapi.entity.note.Note;

public class NoteOrderItemUpdateModel {
    private Long id;
    private Long lastOrderUpdate;
    private Integer order;
    private String columnTitle;

    public NoteOrderItemUpdateModel(Note note) {
        this.id = note.getId();
        this.order = note.getOrder();
        this.lastOrderUpdate = note.getLastOrderUpdate().getTime();
        this.columnTitle = note.getNoteColumn().getTitle();
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

    public String getColumnTitle() {
        return columnTitle;
    }

    public void setColumnTitle(String columnTitle) {
        this.columnTitle = columnTitle;
    }
}
