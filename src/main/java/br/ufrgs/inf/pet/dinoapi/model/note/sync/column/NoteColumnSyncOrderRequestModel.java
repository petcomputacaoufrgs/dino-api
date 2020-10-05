package br.ufrgs.inf.pet.dinoapi.model.note.sync.column;

import br.ufrgs.inf.pet.dinoapi.constants.NoteColumnConstants;

import javax.validation.constraints.NotNull;

public class NoteColumnSyncOrderRequestModel {

    @NotNull(message = NoteColumnConstants.ID_NULL_MESSAGE)
    private Long id;

    @NotNull(message = NoteColumnConstants.ORDER_NULL_MESSAGE)
    private Integer order;

    @NotNull(message = NoteColumnConstants.LAST_UPDATE_ORDER_NULL_MESSAGE)
    private Long lastOrderUpdate;

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
