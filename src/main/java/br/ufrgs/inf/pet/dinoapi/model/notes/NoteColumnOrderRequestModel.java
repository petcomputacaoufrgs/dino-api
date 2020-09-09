package br.ufrgs.inf.pet.dinoapi.model.notes;

import javax.validation.constraints.NotNull;

public class NoteColumnOrderRequestModel {
    private Long id;

    @NotNull(message = "order cannot be null.")
    private Integer order;

    @NotNull(message = "columnTitle cannot be null.")
    private String columnTitle;

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
}
