package br.ufrgs.inf.pet.dinoapi.model.note;

import br.ufrgs.inf.pet.dinoapi.constants.NoteConstants;
import br.ufrgs.inf.pet.dinoapi.model.synchronizable.SynchronizableDataModel;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class NoteDataModel extends SynchronizableDataModel<Long> {
    @NotNull(message = NoteConstants.ORDER_NULL_MESSAGE)
    private Integer order;

    @NotNull(message = NoteConstants.QUESTION_NULL_MESSAGE)
    @Size(min = NoteConstants.QUESTION_MIN, max = NoteConstants.QUESTION_MAX, message = NoteConstants.QUESTION_SIZE_MESSAGE)
    private String question;

    @Size(max = NoteConstants.ANSWER_MAX, message = NoteConstants.ANSWER_SIZE_MESSAGE)
    private String answer;

    @NotNull(message = NoteConstants.COLUMN_ID_NULL_MESSAGE)
    private Long columnId;

    @Size(max = NoteConstants.MAX_TAGS_SIZE, message = NoteConstants.MAX_TAGS_MESSAGE)
    private String tags;

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Long getColumnId() {
        return columnId;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
