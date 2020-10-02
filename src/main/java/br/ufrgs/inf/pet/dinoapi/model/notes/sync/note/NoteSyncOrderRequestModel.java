package br.ufrgs.inf.pet.dinoapi.model.notes.sync.note;

import br.ufrgs.inf.pet.dinoapi.constants.NoteConstants;

import javax.validation.constraints.NotNull;

public class NoteSyncOrderRequestModel {
    @NotNull(message = NoteConstants.ID_NULL_MESSAGE)
    private Long id;

    @NotNull(message = NoteConstants.ORDER_NULL_MESSAGE)
    private Integer order;

    @NotNull(message = NoteConstants.LAST_UPDATE_ORDER_NULL_MESSAGE)
    private Long lastOrderUpdate;

    @NotNull(message = NoteConstants.COLUMN_ID_NULL_MESSAGE)
    private Long columnId;

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

    public Long getColumnId() {
        return columnId;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
    }
}
