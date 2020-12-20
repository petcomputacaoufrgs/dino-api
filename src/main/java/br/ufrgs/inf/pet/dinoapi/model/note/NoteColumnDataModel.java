package br.ufrgs.inf.pet.dinoapi.model.note;

import br.ufrgs.inf.pet.dinoapi.constants.NoteConstants;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataLocalIdModel;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class NoteColumnDataModel extends SynchronizableDataLocalIdModel<Long, Integer> {
    @NotNull(message = NoteConstants.COLUMN_TITLE_NULL_MESSAGE)
    @Size(min = NoteConstants.COLUMN_TITLE_MIN, max = NoteConstants.COLUMN_TITLE_MAX, message = NoteConstants.COLUMN_TITLE_SIZE_MESSAGE)
    private String title;

    @NotNull(message = NoteConstants.ORDER_NULL_MESSAGE)
    private Integer order;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
