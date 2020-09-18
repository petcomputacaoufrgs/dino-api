package br.ufrgs.inf.pet.dinoapi.model.notes;

import br.ufrgs.inf.pet.dinoapi.entity.note.NoteColumn;

import java.util.Date;

public class NoteColumnResponseModel {

    private Long id;

    private String title;

    private Integer order;

    private Long lastUpdate;

    private Long lastOrderUpdate;

    public NoteColumnResponseModel() {}

    public NoteColumnResponseModel(NoteColumn noteColumn) {
        this.id = noteColumn.getId();
        this.title = noteColumn.getTitle();
        this.order = noteColumn.getOrder();
        this.lastUpdate = noteColumn.getLastUpdate().getTime();
        this.lastOrderUpdate = noteColumn.getLastOrderUpdate().getTime();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Long getLastOrderUpdate() {
        return lastOrderUpdate;
    }

    public void setLastOrderUpdate(Long lastOrderUpdate) {
        this.lastOrderUpdate = lastOrderUpdate;
    }
}
