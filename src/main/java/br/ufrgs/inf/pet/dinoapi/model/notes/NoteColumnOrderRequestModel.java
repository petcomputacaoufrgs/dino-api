package br.ufrgs.inf.pet.dinoapi.model.notes;

import br.ufrgs.inf.pet.dinoapi.constants.NoteColumnConstants;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class NoteColumnOrderRequestModel {
    private Long id;

    @NotNull(message = NoteColumnConstants.ORDER_NULL_MESSAGE)
    private Integer order;

    @Size(min = NoteColumnConstants.TITLE_MIN, max = NoteColumnConstants.TITLE_MAX, message = NoteColumnConstants.TITLE_SIZE_MESSAGE)
    @NotNull(message = NoteColumnConstants.TITLE_NULL_MESSAGE)
    private String columnTitle;

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
