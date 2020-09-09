package br.ufrgs.inf.pet.dinoapi.model.notes;

import javax.validation.constraints.NotNull;

import static br.ufrgs.inf.pet.dinoapi.constants.NoteConstants.ORDER_NULL_MESSAGE;

public class NoteSaveRequestRequestModel extends NoteQuestionRequestModel {

    @NotNull(message = ORDER_NULL_MESSAGE)
    private Integer order;

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

}
