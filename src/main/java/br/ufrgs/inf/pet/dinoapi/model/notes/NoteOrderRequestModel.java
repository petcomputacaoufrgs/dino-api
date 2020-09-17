package br.ufrgs.inf.pet.dinoapi.model.notes;

import javax.validation.constraints.NotNull;

import static br.ufrgs.inf.pet.dinoapi.constants.NoteConstants.*;

public class NoteOrderRequestModel {

    @NotNull(message = ID_NULL_MESSAGE)
    private Long id;

    @NotNull(message = ORDER_NULL_MESSAGE)
    private Integer order;

    @NotNull(message = COLUMN_TITLE_NULL_MESSAGE)
    private String columnTitle;

    @NotNull(message = LAST_ORDER_UPDATE_NULL_MESSAGE)
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

    public String getColumnTitle() {
        return columnTitle;
    }

    public void setColumnTitle(String columnTitle) {
        this.columnTitle = columnTitle;
    }

    public Long getLastOrderUpdate() {
        return lastOrderUpdate;
    }

    public void setLastOrderUpdate(Long lastOrderUpdate) {
        this.lastOrderUpdate = lastOrderUpdate;
    }
}
