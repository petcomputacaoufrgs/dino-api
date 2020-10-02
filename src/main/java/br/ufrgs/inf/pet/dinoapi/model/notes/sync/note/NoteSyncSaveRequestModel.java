package br.ufrgs.inf.pet.dinoapi.model.notes.sync.note;

import br.ufrgs.inf.pet.dinoapi.constants.NoteConstants;
import br.ufrgs.inf.pet.dinoapi.model.notes.NoteSaveBaseModel;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class NoteSyncSaveRequestModel extends NoteSaveBaseModel {

    @NotNull(message = NoteConstants.QUESTION_NULL_MESSAGE)
    @Size(min=NoteConstants.QUESTION_MIN, max = NoteConstants.QUESTION_MAX, message = NoteConstants.QUESTION_SIZE_MESSAGE)
    protected String question;

    @NotNull(message = NoteConstants.LAST_UPDATE_NULL_MESSAGE)
    protected Long lastUpdate;

    protected Long lastOrderUpdate;

    @Size(max = NoteConstants.ANSWER_MAX, message = NoteConstants.ANSWER_SIZE_MESSAGE)
    protected String answer;

    protected Integer order;

    @NotNull(message = NoteConstants.COLUMN_ID_NULL_MESSAGE)
    protected Long columnId;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Long getLastOrderUpdate() {
        return lastOrderUpdate;
    }

    public void setLastOrderUpdate(Long lastOrderUpdate) {
        this.lastOrderUpdate = lastOrderUpdate;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Long getColumnId() {
        return columnId;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
    }
}
