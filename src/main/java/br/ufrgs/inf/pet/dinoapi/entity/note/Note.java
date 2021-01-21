package br.ufrgs.inf.pet.dinoapi.entity.note;

import br.ufrgs.inf.pet.dinoapi.constants.NoteConstants;
import br.ufrgs.inf.pet.dinoapi.entity.synchronizable.SynchronizableEntity;

import javax.persistence.*;

@Entity
@Table(name = "note")
public class Note extends SynchronizableEntity<Long> {
    @Column(name = "n_order", nullable = false)
    private Integer order;

    @Column(name = "question", length = NoteConstants.QUESTION_MAX, nullable = false)
    private String question;

    @Column(name = "answer", length = NoteConstants.ANSWER_MAX)
    private String answer;

    @ManyToOne
    @JoinColumn(name = "note_column_id", nullable = false)
    private NoteColumn noteColumn;

    @Column(name = "tags", length = NoteConstants.MAX_TAGS_SIZE)
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

    public NoteColumn getNoteColumn() {
        return noteColumn;
    }

    public void setNoteColumn(NoteColumn noteColumn) {
        this.noteColumn = noteColumn;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
