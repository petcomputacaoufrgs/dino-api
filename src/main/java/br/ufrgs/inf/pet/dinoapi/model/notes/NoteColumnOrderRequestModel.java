package br.ufrgs.inf.pet.dinoapi.model.notes;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class NoteColumnOrderRequestModel {
    private Long id;

    @NotNull(message = "order cannot be null.")
    private Integer order;

    @Size(min = 1, message = "columnTitle cannot be blank.")
    @NotNull(message = "columnTitle cannot be null.")
    private String columnTitle;

    @NotNull(message = "lastOrderUpdate cannot be null.")
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
