package br.ufrgs.inf.pet.dinoapi.model.notes;

import javax.validation.constraints.NotNull;

import static br.ufrgs.inf.pet.dinoapi.constants.NoteConstants.ID_NULL_MESSAGE;
import static br.ufrgs.inf.pet.dinoapi.constants.NoteConstants.ORDER_NULL_MESSAGE;

public class NoteOrderRequestModel {

    @NotNull(message = ID_NULL_MESSAGE)
    private Long id;

    @NotNull(message = ORDER_NULL_MESSAGE)
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
