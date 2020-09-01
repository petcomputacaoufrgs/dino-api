package br.ufrgs.inf.pet.dinoapi.model.notes;

import javax.validation.constraints.NotNull;

public class NoteSaveRequestModel extends NoteQuestionRequestModel {

    @NotNull(message = "order cannot be null.")
    private Integer order;

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

}
