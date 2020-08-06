package br.ufrgs.inf.pet.dinoapi.model.notes;

import javax.validation.constraints.NotNull;

public class NoteOrderRequestModel {

    @NotNull(message = "id cannot be null.")
    private Long id;

    @NotNull(message = "order cannot be null.")
    private Integer order;

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
}
